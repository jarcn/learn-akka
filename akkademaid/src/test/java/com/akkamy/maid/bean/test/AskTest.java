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
import com.akkamy.maid.actor.AskDemoArticleParserActor;
import com.akkamy.maid.actor.ParsingActor;
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

    /**
     * Ask:向 Actor 发送一条消息，返回一个 Future。当 Actor 返回响应时，会完成
     * Future。不会向消息发送者的邮箱返回任何消息。
     * Tell。向 Actor 发送一条消息。所有发送至 sender()的响应都会返回给发送消息的
     * Actor。
     * Forward:将接收到的消息再发送给另一个 Actor。所有发送至 sender()的响应都
     * 会返回给原始消息的发送者。
     * Pipe:用于将 Future 的结果返回给 sender()或另一个 Actor。如果正在使用 Ask
     * 或是处理一个 Future，那么使用 Pipe 可以正确地返回 Future 的结果。
     */

    @Test
    public void itShouldParseArticleTest() throws Exception {
        Future f = Patterns.ask(askDemoActor, new ParseArticle(("http://www.google.com")), timeout);
        cacheProbe.expectMsgClass(GetRequest.class);
        cacheProbe.reply(new Status.Failure(new Exception("no cache")));

        httpClientProbe.expectMsgClass(String.class);
        httpClientProbe.reply(new HttpResponse(Articles.article1));

        String result = (String) Await.result(f, timeout.duration());
        assert(result.contains("I’ve been writing a lot in emacs lately"));
        assert(!result.contains("<body>"));
    }
}
