package view.edit.input.gaad_db;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mahim on 3/11/2018.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.CustomVH> {

    private Context mContext;
    private List<UserInfoModel> employeeModels;

    private OnItemClick onItemClick;

    public interface OnItemClick {
        void onGetItem(String id);
    }

    public EmployeeAdapter(Context mContext, List<UserInfoModel> employeeModels,
                           OnItemClick onItemClick) {
        this.mContext = mContext;
        this.employeeModels = employeeModels;
        this.onItemClick = onItemClick;
    }

    @Override
    public CustomVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(CustomVH holder, int position) {
        final UserInfoModel model = employeeModels.get(position);
        holder.name.setText(model.getUserName());
        holder.designation.setText(model.getUserContactNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onGetItem(model.getUserContactNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeModels.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        TextView name, designation;

        public CustomVH(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.text_view_name);
            designation = itemView.findViewById(R.id.text_view_designation);
        }
    }
}
