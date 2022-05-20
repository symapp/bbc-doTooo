package ch.bbcag.dotooo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import ch.bbcag.dotooo.entity.Color;

public class FilterViewModel extends ViewModel {
    private final MutableLiveData<Color> selectedColor = new MutableLiveData<>();
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> selectedOnlyUncompleted = new MutableLiveData<>();

    public void setSelectedColor(Color color) {
        selectedColor.setValue(color);
    }

    public LiveData<Color> getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedDate(Date date) {
        selectedDate.setValue(date);
    }

    public MutableLiveData<Date> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedOnlyUncompleted(Boolean b) {
        selectedOnlyUncompleted.setValue(b);
    }

    public MutableLiveData<Boolean> getSelectedOnlyUncompleted() {
        return selectedOnlyUncompleted;
    }
}
