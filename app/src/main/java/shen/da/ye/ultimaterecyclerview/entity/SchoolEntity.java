package shen.da.ye.ultimaterecyclerview.entity;

/**
 * @author ChenYe
 *         created by on 2017/12/8 0008. 09:08
 *         学校
 **/

public class SchoolEntity extends AbstractExpandableItem implements MultiItemEntity {

    private String schoolName;

    public SchoolEntity(String name) {
        this.schoolName = name;
    }

    public String getName() {
        return schoolName;
    }

    public void setName(String name) {
        this.schoolName = name;
    }

    @Override
    public int getItemType() {
        return 1;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
