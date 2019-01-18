package com.java8.ch4;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

/**
 * @author chenjia on 2019/1/18
 */
public class PuttingIntoPractice {

    public static void main(String... args) {

        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
          new Transaction(brian, 2011, 300),
          new Transaction(raoul, 2012, 1000),
          new Transaction(raoul, 2011, 400),
          new Transaction(mario, 2012, 710),
          new Transaction(mario, 2012, 700),
          new Transaction(alan, 2012, 950)
        );

        //查询所有 2011年的交易 按value排序
        List<Transaction> tr2011 = transactions.stream()
          .filter(transaction -> transaction.getYear() == 2011)
          .sorted(comparing(Transaction::getValue))
          .collect(toList());
        System.out.println(tr2011);

        // 查询所有交易所在城市（去重）
        List<String> cities =
          transactions.stream()
            .map(transaction -> transaction.getTrader().getCity())
            .distinct()
            .collect(toList());
        System.out.println(cities);

        //所有交易城市为"Cambridge"的，按交易名称排序，去重
        List<Trader> traders =
          transactions.stream()
            .map(Transaction::getTrader)
            .filter(trader -> trader.getCity().equals("Cambridge"))
            .distinct()
            .sorted(comparing(Trader::getName))
            .collect(toList());
        System.out.println(traders);

        //聚合所有交易名称,去重,排序
        String traderStr =
          transactions.stream()
            .map(transaction -> transaction.getTrader().getName())
            .distinct()
            .sorted()
            .reduce("", (n1, n2) -> n1 + n2);
        System.out.println(traderStr);

        //匹配交易所在城市为"Milan"
        boolean milanBased =
          transactions.stream()
            .anyMatch(transaction -> transaction.getTrader()
              .getCity()
              .equals("Milan")
            );
        System.out.println(milanBased);

        // 将"Milan"修改为"Cambridge"
        transactions.stream()
          .map(Transaction::getTrader)
          .filter(trader -> trader.getCity().equals("Milan"))
          .forEach(trader -> trader.setCity("Cambridge"));
        System.out.println(transactions);

        //查询所有交易中最大交易额
        int highestValue =
          transactions.stream()
            .map(Transaction::getValue)
            .reduce(0, Integer::max);
        System.out.println(highestValue);
    }

}
