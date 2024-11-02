package org.example;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HomeWork {

    //Действия для перовой задачи в зависимости от операции - размещение заявки, удаление заявки, продажа
    private final Map<String, BiFunction<ProfitData, String[], ProfitData>> profitAction = Map.of(
            "BID", HomeWork::bid,
            "DEL", HomeWork::del,
            "SALE", HomeWork::sale
    );

    //Действия для второй задачи - добавление или удаление
    private final Map<Predicate<LeaveOrderData>, Consumer<LeaveOrderData>> leaveAction = Map.of(
            (data) -> data.getCurrentAction().equals("+"), HomeWork::add,
            (data) -> data.getCurrentAction().equals("?"), HomeWork::next
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

    //Действия при размещении заявки
    private static ProfitData bid(ProfitData data, String[] actionArray) {
        data.getTreap().add(Double.valueOf(actionArray[1]), data.getCount());
        data.setCount(data.getCount() + 1);
        return data;
    }

    //Действия при снятии заявки
    private static ProfitData del(ProfitData data, String[] actionArray) {
        data.getTreap().remove(Double.valueOf(actionArray[1]));
        return data;
    }

    //Действия при продаже
    private static ProfitData sale(ProfitData data, String[] actionArray) {
        var node = data.getTreap().split(Double.valueOf(actionArray[1]));
        var changeResult = Math.min(
                data.getTreap().inorder(node[1]).size() * 0.01,
                Integer.parseInt(actionArray[2]) * 0.01
        );
        data.setProfit(data.getProfit() + changeResult);
        return data;
    }

    //Данные для первой задачи
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
        LeaveOrderData data = new LeaveOrderData();
        actionList.forEach(action -> {
            String[] actionArray = action.split(" ");
            data.setCurrentAction(actionArray[0]);
            data.setCurrentValue(Integer.parseInt(actionArray[1]));
            leaveAction.forEach((k, v) -> {
                if (k.test(data)) v.accept(data);
            });
        });
        return data.getResult();
    }

    //Добавляем элемент. Если прошлое действие ?, то добавим i + y, если нет - то добавляем i
    private static void add(LeaveOrderData data) {
        if (data.getLastAction().equals("?")) {
            addIY(data);
        } else {
            addI(data);
        }
    }

    //Добавляем в дерево i + y, обновляем сопутствующие данные по задаче
    private static void addIY(LeaveOrderData data) {
        data.setCount(data.getCount() + 1);
        data.getTreap().add(data.getCurrentValue() + data.lastValue, data.getCount());
        data.setLastAction(data.getCurrentAction());
        data.setLastValue(data.getCurrentValue());
    }

    //Добавляем в дерево i, обновляем сопутствующие данные по задаче
    private static void addI(LeaveOrderData data) {
        data.setCount(data.getCount() + 1);
        data.getTreap().add(data.getCurrentValue(), data.getCount());
        data.setLastAction(data.getCurrentAction());
        data.setLastValue(data.getCurrentValue());
    }

    //Добавляем значение в результат, обновляем сопутствующие данные по задаче
    private static void next(LeaveOrderData data) {
        var node = data.getTreap().searchNodeByKey(data.getCurrentValue());
        data.setLastAction(data.getCurrentAction());
        data.setLastValue(node.getKey());
        data.getResult().add(node.getKey());
    }

    //объект данных для второй задачи
    @Getter
    @Setter
    public static class LeaveOrderData {
        private ArrayList<Integer> result = new ArrayList<>();
        private Treap<Integer> treap = new Treap<>();
        private String lastAction = "";
        private Integer lastValue = 0;
        private Integer count = 0;
        private String currentAction = "";
        private Integer currentValue = 0;
    }

}
