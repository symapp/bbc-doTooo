package ch.bbcag.dotooo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ch.bbcag.dotooo.entity.Color;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<Color> selectedColor = new MutableLiveData<>();

    public void setData(Color color) {
        selectedColor.setValue(color);
    }

    public LiveData<Color> getSelectedColor() {
        return selectedColor;
    }
}
