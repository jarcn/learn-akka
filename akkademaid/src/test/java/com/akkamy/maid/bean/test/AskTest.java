package com.akkamy.maid.bean.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.pattern.Patterns;
import akka.testkit.TestProbe;
import akka.util.Timeout;
import com.akkamy.maid.bean.HttpResponse;
import com.akkamy.maid.bean.ParseArticle;
import com.akkamy.maid.bean.actor.AskDemoArticleParserActor;
import com.akkamy.maid.bean.actor.ParsingActor;
import com.akkamy.messages.GetRequest;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;

public class AskTest {
    ActorSystem system = ActorSystem.create("chenjia");
    Timeout timeout = Timeout.longToTimeout(10000);

    TestProbe cacheProbe = new TestProbe(system);
    TestProbe httpClientProbe = new TestProbe(system);
    ActorRef articleParseActor = system.actorOf(Props.create(ParsingActor.class));

    ActorRef askDemoActor = system.actorOf(
      Props.create(AskDemoArticleParserActor.class,
        cacheProbe.ref().path().toString(),
        httpClientProbe.ref().path().toString(),
        articleParseActor.path().toString(),
        timeout)
    );


    @Test
    public void itShouldParseArticleTest() throws Exception {
        Future f = Patterns.ask(askDemoActor, new ParseArticle(("http://www.baidu.com")), timeout);
        cacheProbe.expectMsgClass(GetRequest.class);
        cacheProbe.reply(new Status.Failure(new Exception("no cache")));

        httpClientProbe.expectMsgClass(String.class);
        httpClientProbe.reply(new HttpResponse(Articles.article1));

        String result = (String) Await.result(f, timeout.duration());
        assert(result.contains("I’ve been writing a lot in emacs lately"));
        assert(!result.contains("<body>"));
    }
}
