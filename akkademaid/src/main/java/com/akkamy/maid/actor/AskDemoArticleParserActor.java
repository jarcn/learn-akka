package com.akkamy.maid.actor;


import static scala.compat.java8.FutureConverters.toJava;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.akkamy.maid.bean.ArticleBody;
import com.akkamy.maid.bean.HttpResponse;
import com.akkamy.maid.bean.ParseArticle;
import com.akkamy.maid.bean.ParseHtmlArticle;
import com.akkamy.messages.GetRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import scala.PartialFunction;

/**
 * @author chenjia on 2019/1/13
 */
public class AskDemoArticleParserActor extends AbstractActor {

    private final ActorSelection cacheActor;
    private final ActorSelection httpClientActor;
    private final ActorSelection artcileParseActor;
    private final Timeout timeout;

    public AskDemoArticleParserActor(String cacheActorPath, String httpClientActorPath
      , String artcileParseActorPath, Timeout timeout) {
        this.cacheActor = context().actorSelection(cacheActorPath);
        this.httpClientActor = context().actorSelection(httpClientActorPath);
        this.artcileParseActor = context().actorSelection(artcileParseActorPath);
        this.timeout = timeout;
    }

    @Override
    public PartialFunction receive() {
        return ReceiveBuilder.match(ParseArticle.class, msg -> {
              final CompletionStage cacheResult = toJava(Patterns.ask(cacheActor, new GetRequest(msg.url), timeout));
              final CompletionStage result = cacheResult.handle((x, t) -> {
                  return (x != null)
                    ? CompletableFuture.completedFuture(x)
                    : toJava(Patterns.ask(httpClientActor, msg.url, timeout))
                      .thenCompose(rawArticle -> toJava(
                        Patterns.ask(artcileParseActor, new ParseHtmlArticle(msg.url, ((HttpResponse) rawArticle).body),timeout))
                      );
              }).thenCompose(x -> x);

              final ActorRef senderRef = sender();
              result.handle((x, t) -> {
                  if (x != null) {
                      if (x instanceof ArticleBody) {
                          String body = ((ArticleBody) x).body;
                          cacheActor.tell(body, self());
                          senderRef.tell(body, self());
                      } else if (x instanceof String) {
                          senderRef.tell(x, self());
                      }
                  } else if (x == null) {
                      senderRef.tell(new Status.Failure((Throwable) t), self());
                  }
                  return null;
              });

          }).build();
    }
}
