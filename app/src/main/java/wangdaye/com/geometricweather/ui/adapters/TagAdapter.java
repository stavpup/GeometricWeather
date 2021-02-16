package wangdaye.com.geometricweather.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.ui.widgets.TagView;
import wangdaye.com.geometricweather.utils.managers.ThemeManager;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private final List<Tag> mTagList;
    private @ColorInt final int mCheckedBackgroundColor;
    private final OnTagCheckedListener mListener;
    private @Nullable final ThemeManager mThemeManager;
    private int mCheckedIndex;

    public static final int UNCHECKABLE_INDEX = -1;

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TagView mTagView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTagView = itemView.findViewById(R.id.item_tag);
            mTagView.setOnClickListener(v -> {
                boolean consumed = false;
                if (mListener != null) {
                    consumed = mListener.onItemChecked(!mTagView.isChecked(), mCheckedIndex, getAdapterPosition());
                }
                if (!consumed && mCheckedIndex != getAdapterPosition()) {
                    int i = mCheckedIndex;
                    mCheckedIndex = getAdapterPosition();
                    notifyItemChanged(i);
                    notifyItemChanged(mCheckedIndex);
                }
            });
        }

        void onBindView(Tag tag, boolean checked) {
            mTagView.setText(tag.getName());
            setChecked(checked);

            if (mThemeManager != null) {
                mTagView.setCheckedBackgroundColor(mCheckedBackgroundColor);
                mTagView.setUncheckedBackgroundColor(mThemeManager.getLineColor(mTagView.getContext()));
            }
        }

        public void setChecked(boolean checked) {
            mTagView.setChecked(checked);
            if (mThemeManager != null) {
                if (checked) {
                    mTagView.setTextColor(mThemeManager.getTextContentColor(mTagView.getContext()));
                } else {
                    mTagView.setTextColor(mThemeManager.getTextSubtitleColor(mTagView.getContext()));
                }
            }
        }
    }

    public interface OnTagCheckedListener {
        boolean onItemChecked(boolean checked, int oldPosition, int newPosition);
    }

    public interface Tag {
        String getName();
    }

    public TagAdapter(Context context, List<Tag> tagList, OnTagCheckedListener listener) {
        this(context, tagList, Color.TRANSPARENT, listener, UNCHECKABLE_INDEX);
    }

    public TagAdapter(Context context, List<Tag> tagList, @ColorInt int checkedBackgroundColor,
                      OnTagCheckedListener listener, int checkedIndex) {
        mTagList = tagList;
        mCheckedBackgroundColor = checkedBackgroundColor;
        mListener = listener;
        mThemeManager = ThemeManager.getInstance(context);
        mCheckedIndex = checkedIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_tag, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(mTagList.get(position), position == mCheckedIndex);
    }

    @Override
    public int getItemCount() {
        return mTagList.size();
    }

    public void insertItem(Tag tag) {
        mTagList.add(tag);
        notifyItemInserted(mTagList.size() - 1);
    }

    public Tag removeItem(int position) {
        Tag tag = mTagList.remove(position);
        notifyItemRemoved(position);
        return tag;
    }
}