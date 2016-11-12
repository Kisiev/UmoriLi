package list.umorili.android.com.umorili.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private SparseBooleanArray selectedItems;

    public SelectableAdapter() {
        selectedItems = new SparseBooleanArray();
    }

    //получаем список items в виде списка типа Integer
    //задача: интерпретировать каждую позицию в boolean
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    //проверяет выделен ли item
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    //нам нужно знать сколько элементов нужно отобразить. В каждом адаптере есть метод, который возвращает
    //колличество элементов которые засетили список(попали в дата сет)
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public void toggleSelection(int position) {
        //передаем позицию и значению boolean по умолчанию - внутрь нашей коллекции
        //если по этой позиции что-то лежит, то мы должны ее удалить
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else { //иначе, мы должны ее поместить
            selectedItems.put(position, true);
        }

        //так как мы эксендим RecyclerView нам доступен метод - сообщить адаптеру, что состояние
        //item поменялось. Передаем внутрь position
        notifyItemChanged(position);
    }
    //получаем все выбранные itmes и чистим
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();

        for (Integer i : selection) {
            //сообщаем адаптеру что состояние нашего дата сета поменялось
            notifyItemChanged(i);
        }
    }

}
