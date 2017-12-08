package shen.da.ye.ultimaterecyclerview.entity;

/**
 * @author ChenYe
 *         created by on 2017/12/8 0008. 09:10
 *         班级
 **/

public class ClassEntity extends AbstractExpandableItem implements MultiItemEntity {

    private String className;

    public ClassEntity(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public int getItemType() {
        return 4;
    }


    @Override
    public int getLevel() {
        return 4;
    }
}
