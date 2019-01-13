package com.akkamy.maid.bean.actor;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import com.akkamy.maid.bean.ArticleBody;
import com.akkamy.maid.bean.ParseHtmlArticle;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import scala.PartialFunction;

/**
 * @author chenjia on 2019/1/13
 */
public class ParsingActor extends AbstractActor {

    /**
     * 解析
     */
    @Override
    public PartialFunction receive() {
        return ReceiveBuilder.
          match(ParseHtmlArticle.class, msg -> {
              String body = ArticleExtractor.INSTANCE.getText(msg.getHtmlString());
              sender().tell(new ArticleBody(msg.getUri(), body), self());
          }).build();
    }

}
