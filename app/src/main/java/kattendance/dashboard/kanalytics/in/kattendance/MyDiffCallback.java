package kattendance.dashboard.kanalytics.in.kattendance;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class MyDiffCallback extends DiffUtil.Callback{
  List<Backlockmodel> oldPersons;
  List<Backlockmodel> newPersons;

  public MyDiffCallback(List<Backlockmodel> newPersons, List<Backlockmodel> oldPersons) {
    this.newPersons = newPersons;
    this.oldPersons = oldPersons;
  }
  @Override
  public int getOldListSize() {
    return oldPersons.size();
  }

  @Override
  public int getNewListSize() {
    return newPersons.size();
  }

  @Override
  public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    return oldPersons.get(oldItemPosition).getDate() == newPersons.get(newItemPosition).getDate();
  }

  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    return oldPersons.get(oldItemPosition).equals(newPersons.get(newItemPosition));
  }

  @Nullable
  @Override
  public Object getChangePayload(int oldItemPosition, int newItemPosition) {
    //you can return particular field for changed item.
    return super.getChangePayload(oldItemPosition, newItemPosition);
  }
}
