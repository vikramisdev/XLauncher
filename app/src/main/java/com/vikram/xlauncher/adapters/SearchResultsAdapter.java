package com.vikram.xlauncher.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.vikram.xlauncher.adapters.SearchResultsAdapter.ViewHolder;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.vikram.xlauncher.R;



public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

  private List<String> dataList;

  public SearchResultsAdapter(List<String> dataList) {
    this.dataList = dataList;
  }

  @NotNull
  @Override
  public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
    String item = dataList.get(position);
    holder.bind(item);
  }

  @Override
  public int getItemCount() {
    return dataList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public ViewHolder(@NotNull View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.textView);
    }

    public void bind(String item) {
      textView.setText(item);
    }
  }
}
