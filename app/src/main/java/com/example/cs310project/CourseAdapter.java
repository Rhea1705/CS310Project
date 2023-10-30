package com.example.cs310project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> itemList;
    private List<Course> courseList;
    private OnItemClickListener onItemClickListener;

    public CourseAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }

    // Define view types for departments and courses
    private static final int VIEW_TYPE_DEPARTMENT = 0;
    private static final int VIEW_TYPE_COURSE = 1;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof Department) {
            return VIEW_TYPE_DEPARTMENT;
        } else {
            return VIEW_TYPE_COURSE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_DEPARTMENT) {
            View view = inflater.inflate(R.layout.department_item, parent, false);
            return new DepartmentViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.course_item, parent, false);
            return new CourseViewHolder(view);
        }
    }
    //    @NonNull
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = itemList.get(holder.getAdapterPosition());

        if (item instanceof Department) {
            Department department = (Department) item;
            DepartmentViewHolder departmentViewHolder = (DepartmentViewHolder) holder;
            departmentViewHolder.departmentNameTextView.setText(department.getName());

            departmentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(holder.getAdapterPosition());
                    }
                }
            });
        } else if (item instanceof Course) {
            Course course = (Course) item;
            CourseViewHolder courseViewHolder = (CourseViewHolder) holder;
            courseViewHolder.courseNameTextView.setText(course.getName());
            courseViewHolder.courseDescriptionTextView.setText(course.getDescription());


            if (course.isExpanded()) {
                courseViewHolder.courseDescriptionTextView.setVisibility(View.VISIBLE);
                courseViewHolder.courseEnrollmentTextView.setVisibility(View.VISIBLE);
                courseViewHolder.courseDescriptionTextView.setText(course.getDescription());
                courseViewHolder.courseEnrollmentTextView.setText("Enrolled: " + course.getNumEnrolled());
            } else {
                courseViewHolder.courseDescriptionTextView.setVisibility(View.GONE);
                courseViewHolder.courseEnrollmentTextView.setVisibility(View.GONE);
            }



            courseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(holder.getAdapterPosition());
                    }
                }
            });
        }

    }




    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView departmentNameTextView;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentNameTextView = itemView.findViewById(R.id.departmentNameTextView);
        }
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameTextView;
        TextView courseDescriptionTextView;
        TextView courseEnrollmentTextView;


        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
            courseDescriptionTextView = itemView.findViewById(R.id.courseDescriptionTextView);
        }
    }
}

