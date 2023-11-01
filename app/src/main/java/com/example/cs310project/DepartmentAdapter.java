package com.example.cs310project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {
    private List<Department> departmentList;
    private OnItemClickListener onItemClickListener;

    public DepartmentAdapter(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_item, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        Department department = departmentList.get(position);
        holder.departmentNameTextView.setText(department.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView departmentNameTextView;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentNameTextView = itemView.findViewById(R.id.departmentNameTextView);
        }
    }
}