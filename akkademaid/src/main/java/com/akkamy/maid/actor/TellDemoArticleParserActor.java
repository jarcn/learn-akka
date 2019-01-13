package com.akkamy.maid.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;
import akka.util.Timeout;
import com.akkamy.maid.bean.ArticleBody;
import com.akkamy.maid.bean.HttpResponse;
import com.akkamy.maid.bean.ParseArticle;
import com.akkamy.maid.bean.ParseHtmlArticle;
import com.akkamy.messages.GetRequest;
import com.akkamy.messages.SetRequest;
import java.util.concurrent.TimeoutException;
import scala.PartialFunction;

/**
 * @author chenjia on 2019/1/13
 */
public class TellDemoArticleParserActor extends AbstractActor {

    private final ActorSelection cacheActor;
    private final ActorSelection httpClientActor;
    private final ActorSelection artcileParseActor;
    private final Timeout timeout;

    public TellDemoArticleParserActor(String cacheActorPath, String httpClientActorPath, String artcileParseActorPath,
      Timeout timeout) {
        this.cacheActor = context().actorSelection(cacheActorPath);
        this.httpClientActor = context().actorSelection(httpClientActorPath);
        this.artcileParseActor = context().actorSelection(artcileParseActorPath);
        this.timeout = timeout;
    }

    @Override
    public PartialFunction receive() {
        return ReceiveBuilder.
          match(ParseArticle.class, msg -> {
              ActorRef extraActor = buildExtraActor(sender(), msg.url);
              cacheActor.tell(new GetRequest(msg.url), extraActor);
              httpClientActor.tell(msg.url, extraActor);

              context().system().scheduler()
                .scheduleOnce(timeout.duration(), extraActor, "timeout", context().system().dispatcher(),
                  ActorRef.noSender());
          }).build();
    }

    private ActorRef buildExtraActor(ActorRef senderRef, String uri) {

        class MyActor extends AbstractActor {

            public MyActor() {
                receive(ReceiveBuilder
                  .matchEquals(String.class, x -> "timeout".equals(x), x -> {
                      senderRef.tell(new Status.Failure(new TimeoutException("timeout!")), self());
                      context().stop(self());
                  })
                  .match(HttpResponse.class,
                    httpResponse -> {
                        artcileParseActor.tell(new ParseHtmlArticle(uri, httpResponse.body), self());
                    })
                  .match(String.class, body -> {
                      senderRef.tell(body, self());
                      context().stop(self());
                  })
                  .match(ArticleBody.class, articleBody -> {
                      cacheActor.tell(new SetRequest(articleBody.uri, articleBody.body), self());
                      senderRef.tell(articleBody.body, self());
                      context().stop(self());
                  })
                  .matchAny(t -> {
                      System.out.println("ignoring msg: " + t.getClass());
                  })
                  .build());
            }
        }

        return context().actorOf(Props.create(MyActor.class, () -> new MyActor()));


    }
}
