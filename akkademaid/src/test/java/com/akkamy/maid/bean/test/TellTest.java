package com.akkamy.maid.bean.test;

import static akka.pattern.Patterns.ask;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.TestProbe;
import akka.util.Timeout;
import com.akkamy.maid.bean.HttpResponse;
import com.akkamy.maid.bean.ParseArticle;
import com.akkamy.maid.bean.actor.ParsingActor;
import com.akkamy.maid.bean.actor.TellDemoArticleParserActor;
import com.akkamy.messages.GetRequest;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;

/**
 * @author chenjia on 2019/1/13
 */
public class TellTest {

    ActorSystem system = ActorSystem.create("testSystem");
    Timeout timeout = Timeout.longToTimeout(10000);

    TestProbe cacheProbe = new TestProbe(system);
    TestProbe httpClientProbe = new TestProbe(system);
    ActorRef articleParseActor = system.actorOf(Props.create(ParsingActor.class));

    ActorRef tellDemoActor = system.actorOf(
      Props.create(TellDemoArticleParserActor.class,
        cacheProbe.ref().path().toString(),
        httpClientProbe.ref().path().toString(),
        articleParseActor.path().toString(),
        timeout)
    );

    @Test
    public void itShouldParseArticleTest() throws Exception {
        Future f = ask(tellDemoActor, new ParseArticle(("http://www.google.com")), timeout);
        cacheProbe.expectMsgClass(GetRequest.class);
        cacheProbe.reply(new Status.Failure(new Exception("no cache")));

        httpClientProbe.expectMsgClass(String.class);
        httpClientProbe.reply(new HttpResponse(Articles.article1));

        String result = (String) Await.result(f, timeout.duration());
        assert (result.contains("I’ve been writing a lot in emacs lately"));
        assert (!result.contains("<body>"));
    }


}
