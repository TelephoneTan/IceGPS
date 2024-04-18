package pub.telephone.iceGPS.dataSource;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewbinding.ViewBinding;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class EmbeddedDataNode<
        CH extends DataViewHolder<?>,
        VH extends EmbeddedDataNode.ViewHolder<?, CH>,
        INFO,
        CD extends DataNode<CH>
        >
        extends DataNode<VH> implements EmbeddedDataNodeAPI.DataNode<CH, INFO, CD> {
    public static abstract class ViewHolder<CT extends ViewBinding, CH extends DataViewHolder<?>>
            extends DataViewHolder<CT> implements EmbeddedDataNodeAPI.ViewHolder<CH> {
        protected abstract @NotNull ViewGroup retrieveContainer();

        final CH childHolder;

        public ViewHolder(
                @NonNull LayoutInflater inflater,
                @Nullable ViewGroup parent,
                @NotNull Class<CT> containerBindingClass,
                @NotNull EmbeddedDataNodeAPI.ViewHolderCreator<CH> embeddedCreator
        ) {
            super(containerBindingClass, inflater, parent);
            //
            childHolder = embeddedCreator.createChild(inflater, retrieveContainer());
            //
            retrieveContainer().addView(childHolder.itemView);
        }
    }

    protected abstract @NotNull TagKey loadKey();

    final CD childNode;

    public EmbeddedDataNode(
            @Nullable WeakReference<LifecycleOwner> lifecycleOwner,
            @Nullable VH holder,
            @NotNull EmbeddedDataNodeAPI.DataNodeCreator<CH, INFO, CD> embeddedCreator
    ) {
        super(lifecycleOwner, holder);
        //
        childNode = embeddedCreator.createChild(
                lifecycleOwner,
                holder == null ? null : holder.childHolder
        );
        load = bindTask(
                loadKey(),
                RetrySharedTask.Simple(embeddedCreator::load)
        );
    }

    final Binding<Object> init = emptyBinding();
    final Binding<INFO> load;
    final AtomicBoolean loaded = new AtomicBoolean(false);

    protected void init_ui(@NotNull VH holder) {
    }

    protected void loading_ui(@NotNull VH holder) {
    }

    protected void loaded_ui(@NotNull VH holder) {
    }

    @Override
    protected final void __Bind__(Set<Integer> changedBindingKeys) {
        init.Bind(
                changedBindingKeys,
                holder -> {
                    init_ui(holder);
                    return null;
                }, null, null
        );
        load.Bind(
                changedBindingKeys,
                holder -> {
                    loading_ui(holder);
                    initChild_ui(lifecycleOwner, holder.childHolder);
                    return null;
                },
                (holder, info) -> {
                    loaded_ui(holder);
                    childLoaded_ui(lifecycleOwner, holder.childHolder, info);
                    loaded.set(true);
                    return null;
                },
                null
        );
        if (loaded.get()) {
            childNode.EmitChange_ui(null);
        }
    }
}
