package org.example;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class HomeWork {

    private final Map<String, BiFunction<ProfitData, String[], ProfitData>> profitAction = Map.of(
            "BID", HomeWork::bid,
            "DEL", HomeWork::del,
            "SALE", HomeWork::sale
    );

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу
     * <a href="https://acm.timus.ru/problem.aspx?space=1&num=1316">https://acm.timus.ru/problem.aspx?space=1&num=1316</a>
     */
    public Double getProfit(List<String> actionList) {
        var data = new ProfitData();
        actionList.forEach(action -> {
            String[] actionArray = action.split(" ");
            profitAction.get(actionArray[0]).apply(data, actionArray);
        });

        return data.getProfit();
    }

    private static ProfitData bid(ProfitData data, String[] actionArray) {
        data.getTreap().add(Double.valueOf(actionArray[1]), data.getCount());
        data.setCount(data.getCount() + 1);
        return data;
    }

    private static ProfitData del(ProfitData data, String[] actionArray) {
        data.getTreap().remove(Double.valueOf(actionArray[1]));
        return data;
    }

    private static ProfitData sale(ProfitData data, String[] actionArray) {
        var node = data.getTreap().split(Double.valueOf(actionArray[1]));
        var changeResult = Math.min(
                data.getTreap().inorder(node[1]).size() * 0.01,
                Integer.parseInt(actionArray[2]) * 0.01
        );
        data.setProfit(data.getProfit() + changeResult);
        return data;
    }
    @Getter
    @Setter
    public static class ProfitData {
        Treap<Double> treap = new Treap<>();
        Double price = 0.0;
        int count;
        Double profit = 0.0;
    }

    /**
     * <h1>Задание 2.</h1>
     * Решить задачу <br/>
     * <a href="https://informatics.msk.ru/mod/statements/view.php?id=1974&chapterid=2782#1">https://informatics.msk.ru/mod/statements/view.php?id=1974&chapterid=2782#1</a><br/>
     */
    public List<Integer> getLeaveOrder(List<String> actionList) {
        return null;
    }

}
