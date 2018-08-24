package ru.davidlevi.weather.ui.fragments.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.davidlevi.weather.R;

/**
 * Адаптер для RecyclerView. Список городов.
 */
public class AdapterListCities extends RecyclerView.Adapter<AdapterListCities.ViewHolder> {
    // =============================================================================
    // ViewHolder хранит связь между данными и элементами View расположенными на макете.
    //
    // Обрабатывает события виджетов из макета item_listcities_recyclerview.xml привязанного
    // в onCreateViewHolder.
    // =============================================================================
    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView city;

        ViewHolder(View view) {
            super(view);
            this.city = view.findViewById(R.id.item_city_recyclerview);

            city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onCityClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    /**
     * Интерфейс для обработки нажатий на элемент RecyclerView
     *
     * @see ru.davidlevi.weather.ui.fragments.ListCitiesFragments имплементирует данный интерфейс
     */
    public interface OnItemClickListener {
        void onCityClick(int position);
    }

    private OnItemClickListener onItemClickListener;  // Слушатель интерфейса OnItemClickListener

    // Поля:
    private List<String> list;

    /**
     * Сеттер слушателя нажатий на город
     *
     * @param itemPictureClickListener OnItemClickListener
     */
    public void SetOnItemClickListener(OnItemClickListener itemPictureClickListener) {
        this.onItemClickListener = itemPictureClickListener;
    }

    /**
     * Конструктор AdapterListCities
     */
    public AdapterListCities(List<String> list) {
        this.list = list;
    }

    /**
     * Этап 1. Создает ViewHolder из макета item_recyclerview.xml
     * Запускается менеджером!
     *
     * @param parent   ViewGroup
     * @param viewType int
     * @return AdapterListCities.ViewHolder
     */
    @NonNull
    @Override
    public AdapterListCities.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listcities_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Этап 2. Привязка данных к ViewHolder
     * Запускается менеджером!
     *
     * @param holder   ViewHolder
     * @param position int
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.city.setText(list.get(position));
    }

    /**
     * Этап 3. Уведомить о количестве элементов ViewHolder
     * Запускается менеджером!
     *
     * @return int
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}
