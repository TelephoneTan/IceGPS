package pub.telephone.iceGPS.dataSource;

import androidx.viewbinding.ViewBinding;

public class DataViewBinding<B extends ViewBinding> {
    final B binding;

    DataViewBinding(B binding) {
        this.binding = binding;
    }
}
