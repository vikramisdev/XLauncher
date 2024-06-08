package com.vikram.xlauncher.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.vikram.xlauncher.R;
import com.vikram.xlauncher.adapters.HomepageAdapter.ViewHolder;
import java.util.Collections;
import java.util.List;

public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.ViewHolder> {

  private List<String> dataList;
  private Context context;
  private boolean isTouched = false;
  private boolean isLongPressed = false;
  private boolean isOriginalSizeSet = false;
  private View.OnLongClickListener onLongClick;
  private View.OnClickListener onClick;
  private ViewGroup.LayoutParams originalLayoutParams; // Store original layout params

  public HomepageAdapter(Context context, List<String> dataList) {
    this.context = context;
    this.dataList = dataList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_cards, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    String data = dataList.get(position);
    holder.textView.setText(data);

    // Set onClickListener to handle touch events
    View.OnClickListener onClick =
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            isTouched = true;
            isLongPressed = false; // Reset long press flag
            notifyDataSetChanged();
          }
        };
    holder.itemView.setOnClickListener(onClick);

    // Set onLongClickListener to handle long click events
    onLongClick =
        new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            isLongPressed = true;
            isTouched = false; // Reset touch flag
            notifyDataSetChanged();
            return false; // Consume long click event
          }
        };
    holder.itemView.setOnLongClickListener(onLongClick);
	
    // Adjust dimensions based on touch or long press state
    ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
    if (isLongPressed) {
      layoutParams.width = convertDpToPixels(260); // Set width to 260dp
      layoutParams.height = convertDpToPixels(500); // Set height to 500dp
	  
	  holder.itemView.setBackgroundResource(R.drawable.round_rect);
	  // this will fix for the drag and drop
	  holder.itemView.setOnLongClickListener(null);

    } else if (isTouched) {
      if (!isOriginalSizeSet) {
        // Store original layout params
        originalLayoutParams = holder.itemView.getLayoutParams();
        isOriginalSizeSet = true;
      }
      layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
      layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
	  
	  holder.itemView.setBackgroundColor(Color.TRANSPARENT);
    } else {
      // Reset to original dimensions if not touched or long pressed
      if (isOriginalSizeSet) {
        layoutParams.width = originalLayoutParams.width;
        layoutParams.height = originalLayoutParams.height;
      }
    }
	
    holder.itemView.setLayoutParams(layoutParams);
  }

  @Override
  public int getItemCount() {
    return dataList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.item_text);
    }
  }

  public void onItemMove(int fromPosition, int toPosition) {
    if (fromPosition < toPosition) {
      for (int i = fromPosition; i < toPosition; i++) {
        Collections.swap(dataList, i, i + 1);
      }
    } else {
      for (int i = fromPosition; i > toPosition; i--) {
        Collections.swap(dataList, i, i - 1);
      }
    }
    notifyItemMoved(fromPosition, toPosition);
  }

  // Helper method to convert dp to pixels
  private int convertDpToPixels(int dp) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5f);
  }
}
