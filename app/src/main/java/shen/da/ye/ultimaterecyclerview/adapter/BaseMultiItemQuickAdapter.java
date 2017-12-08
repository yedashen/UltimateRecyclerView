package shen.da.ye.ultimaterecyclerview.adapter;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import shen.da.ye.ultimaterecyclerview.MyApplication;
import shen.da.ye.ultimaterecyclerview.R;
import shen.da.ye.ultimaterecyclerview.entity.ClassEntity;
import shen.da.ye.ultimaterecyclerview.entity.CollegeEntity;
import shen.da.ye.ultimaterecyclerview.entity.IExpandable;
import shen.da.ye.ultimaterecyclerview.entity.MultiItemEntity;
import shen.da.ye.ultimaterecyclerview.entity.ProfessionEntity;
import shen.da.ye.ultimaterecyclerview.entity.SchoolEntity;
import shen.da.ye.ultimaterecyclerview.entity.StudentEntity;
import shen.da.ye.ultimaterecyclerview.view.recyclerview.adapter.SuperViewHolder;

/**
 * @author ChenYe
 *         created by on 2017/12/8 0008. 10:28
 **/

public class BaseMultiItemQuickAdapter<T extends MultiItemEntity> extends RecyclerView.Adapter<SuperViewHolder> {

    /**
     * layouts indexed with their types
     */
    private SparseIntArray layouts;
    private List<T> mDataList = new ArrayList<>();
    private static final int DEFAULT_VIEW_TYPE = -0xff;
    public static final int TYPE_NOT_FOUND = -404;


    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return createBaseViewHolder(View.inflate(parent.getContext(), R.layout.item_shcool, null));
            case 2:
                return createBaseViewHolder(View.inflate(parent.getContext(), R.layout.item_college, null));
            case 3:
                return createBaseViewHolder(View.inflate(parent.getContext(), R.layout.item_profession, null));
            case 4:
                return createBaseViewHolder(View.inflate(parent.getContext(), R.layout.item_class, null));
            case 5:
                return createBaseViewHolder(View.inflate(parent.getContext(), R.layout.item_student, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final SuperViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case 1:
                final SchoolEntity schoolEntity = (SchoolEntity) mDataList.get(position);
                holder.setText(R.id.item_school_tv, schoolEntity.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (schoolEntity.isExpanded()) {
                            collapse(pos, true, true);
                        } else {
                            expand(pos, true, true);
                        }
                    }
                });
                break;
            case 2:
                final CollegeEntity collegeEntity = (CollegeEntity) mDataList.get(position);
                holder.setText(R.id.item_college_tv, collegeEntity.getCollegeName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (collegeEntity.isExpanded()) {
                            collapse(pos, true, true);
                        } else {
                            expand(pos, true, true);
                        }
                    }
                });
                break;
            case 3:
                final ProfessionEntity professionEntity = (ProfessionEntity) mDataList.get(position);
                holder.setText(R.id.item_profession_tv, professionEntity.getProfessionName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (professionEntity.isExpanded()) {
                            collapse(pos, true, true);
                        } else {
                            expand(pos, true, true);
                        }
                    }
                });
                break;
            case 4:
                final ClassEntity classEntity = (ClassEntity) mDataList.get(position);
                holder.setText(R.id.item_class_tv, classEntity.getClassName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (classEntity.isExpanded()) {
                            collapse(pos, true, true);
                        } else {
                            expand(pos, true, true);
                        }
                    }
                });
                break;
            case 5:
                final StudentEntity studentEntity = (StudentEntity) mDataList.get(position);
                holder.setText(R.id.item_student_tv, studentEntity.getStudentName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("BaseAdapter", "position" + position);
                        Toast.makeText(MyApplication.mShareInstance, studentEntity.getStudentName(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public int expand(@IntRange(from = 0) int position, boolean animate, boolean shouldNotify) {
        position -= getHeaderLayoutCount();

        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }
        if (!hasSubItems(expandable)) {
            expandable.setExpanded(true);
            notifyItemChanged(position);
            return 0;
        }
        int subItemCount = 0;
        if (!expandable.isExpanded()) {
            List list = expandable.getSubItems();
            mDataList.addAll(position + 1, list);
            subItemCount += recursiveExpand(position + 1, list);

            expandable.setExpanded(true);
        }
        int parentPos = position + getHeaderLayoutCount();
        if (shouldNotify) {
            if (animate) {
                notifyItemChanged(parentPos);
                notifyItemRangeInserted(parentPos + 1, subItemCount);
            } else {
                notifyDataSetChanged();
            }
        }
        return subItemCount;
    }

    @SuppressWarnings("unchecked")
    private int recursiveExpand(int position, @NonNull List list) {
        int count = list.size();
        int pos = position + list.size() - 1;
        for (int i = list.size() - 1; i >= 0; i--, pos--) {
            if (list.get(i) instanceof IExpandable) {
                IExpandable item = (IExpandable) list.get(i);
                if (item.isExpanded() && hasSubItems(item)) {
                    List subList = item.getSubItems();
                    mDataList.addAll(pos + 1, subList);
                    int subItemCount = recursiveExpand(pos + 1, subList);
                    count += subItemCount;
                }
            }
        }
        return count;

    }

    private boolean hasSubItems(IExpandable item) {
        if (item == null) {
            return false;
        }
        List list = item.getSubItems();
        return list != null && list.size() > 0;
    }


    public int collapse(@IntRange(from = 0) int position, boolean animate, boolean notify) {
        position -= getHeaderLayoutCount();
        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }
        int subItemCount = recursiveCollapse(position);
        expandable.setExpanded(false);
        int parentPos = position + getHeaderLayoutCount();
        if (notify) {
            if (animate) {
                notifyItemChanged(parentPos);
                notifyItemRangeRemoved(parentPos + 1, subItemCount);
            } else {
                notifyDataSetChanged();
            }
        }
        return subItemCount;
    }

    private int getHeaderLayoutCount() {
        return 0;
    }

    private IExpandable getExpandableItem(int position) {
        T item = getItem(position);
        if (isExpandable(item)) {
            return (IExpandable) item;
        } else {
            return null;
        }
    }

    private T getItem(int position) {
        return mDataList.get(position);
    }

    public boolean isExpandable(T item) {
        return item != null && item instanceof IExpandable;
    }

    @SuppressWarnings("unchecked")
    private int recursiveCollapse(@IntRange(from = 0) int position) {
        T item = getItem(position);
        if (!isExpandable(item)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        int subItemCount = 0;
        if (expandable.isExpanded()) {
            List<T> subItems = expandable.getSubItems();
            if (null == subItems) {
                return 0;
            }


            for (int i = subItems.size() - 1; i >= 0; i--) {
                T subItem = subItems.get(i);
                int pos = getItemPosition(subItem);
                if (pos < 0) {
                    continue;
                }
                if (subItem instanceof IExpandable) {
                    subItemCount += recursiveCollapse(pos);
                }
                mDataList.remove(pos);
                subItemCount++;
            }
        }
        return subItemCount;
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        Object item = mDataList.get(position);
        if (item instanceof MultiItemEntity) {
            return ((MultiItemEntity) item).getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }


    public void remove(@IntRange(from = 0L) int position) {
        if (mDataList == null || position < 0 || position >= mDataList.size()) {
            return;
        }

        T entity = mDataList.get(position);
        if (entity instanceof IExpandable) {
            removeAllChild((IExpandable) entity, position);
        }
        removeDataFromParent(entity);
    }

    /**
     * 移除父控件时，若父控件处于展开状态，则先移除其所有的子控件
     *
     * @param parent         父控件实体
     * @param parentPosition 父控件位置
     */
    protected void removeAllChild(IExpandable parent, int parentPosition) {
        if (parent.isExpanded()) {
            List<MultiItemEntity> chidChilds = parent.getSubItems();
            if (chidChilds == null || chidChilds.size() == 0) {
                return;
            }

            int childSize = chidChilds.size();
            for (int i = 0; i < childSize; i++) {
                remove(parentPosition + 1);
            }
        }
    }

    /**
     * 移除子控件时，移除父控件实体类中相关子控件数据，避免关闭后再次展开数据重现
     *
     * @param child 子控件实体
     */
    protected void removeDataFromParent(T child) {
        int position = getParentPosition(child);
        if (position >= 0) {
            IExpandable parent = (IExpandable) mDataList.get(position);
            parent.getSubItems().remove(child);
        }
    }

    public int getParentPosition(@NonNull T item) {
        int position = getItemPosition(item);
        if (position == -1) {
            return -1;
        }

        // if the item is IExpandable, return a closest IExpandable item position whose level smaller than this.
        // if it is not, return the closest IExpandable item position whose level is not negative
        int level;
        if (item instanceof IExpandable) {
            level = ((IExpandable) item).getLevel();
        } else {
            level = Integer.MAX_VALUE;
        }
        if (level == 0) {
            return position;
        } else if (level == -1) {
            return -1;
        }

        for (int i = position; i >= 0; i--) {
            T temp = mDataList.get(i);
            if (temp instanceof IExpandable) {
                IExpandable expandable = (IExpandable) temp;
                if (expandable.getLevel() >= 0 && expandable.getLevel() < level) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int getItemPosition(T item) {
        return item != null && mDataList != null && !mDataList.isEmpty() ? mDataList.indexOf(item) : -1;
    }

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    @SuppressWarnings("unchecked")
    protected SuperViewHolder createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        SuperViewHolder k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = new SuperViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : new SuperViewHolder(view);
    }

    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (SuperViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && SuperViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private SuperViewHolder createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (SuperViewHolder) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (SuperViewHolder) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDataList(ArrayList<T> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }
}
