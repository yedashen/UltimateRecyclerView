package shen.da.ye.ultimaterecyclerview.entity;

/**
 * @author ChenYe
 *         created by on 2017/12/8 0008. 14:43
 **/

public class ProfessionEntity extends AbstractExpandableItem implements MultiItemEntity {

    private String professionName;

    public ProfessionEntity(String professionName) {
        this.professionName = professionName;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    @Override
    public int getItemType() {
        return 3;
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public String toString() {
        return "ProfessionEntity{" +
                "professionName='" + professionName + '\'' +
                '}';
    }
}
