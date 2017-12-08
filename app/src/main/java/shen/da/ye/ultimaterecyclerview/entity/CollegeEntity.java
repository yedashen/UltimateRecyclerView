package shen.da.ye.ultimaterecyclerview.entity;

/**
 * @author ChenYe
 *         created by on 2017/12/8 0008. 09:09
 *         学院
 **/

public class CollegeEntity extends AbstractExpandableItem implements MultiItemEntity{

    private String collegeName;

    public CollegeEntity(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    @Override
    public int getItemType() {
        return 2;
    }

    @Override
    public int getLevel() {
        return 2;
    }
}
