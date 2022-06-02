package ch.bbcag.dotooo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import ch.bbcag.dotooo.entity.Color;

public class FilterViewModel extends ViewModel {
    private final MutableLiveData<Color> selectedColor = new MutableLiveData<>();
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<Boolean> selectedOnlyUncompleted = new MutableLiveData<>();
    private final MutableLiveData<String> selectedSearchQuery = new MutableLiveData<>();

    public void setSelectedColor(Color c) {
        selectedColor.setValue(c);
    }

    public LiveData<Color> getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedDate(Date d) {
        selectedDate.setValue(d);
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

    public void setSelectedSearchQuery(String s) {
        selectedSearchQuery.setValue(s);
    }

    public MutableLiveData<String> getSelectedSearchQuery() {
        return selectedSearchQuery;
    }
}
