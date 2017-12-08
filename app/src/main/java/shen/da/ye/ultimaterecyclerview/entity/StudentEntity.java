package shen.da.ye.ultimaterecyclerview.entity;

/**
 * @author ChenYe
 *         created by on 2017/12/8 0008. 14:43
 **/

public class StudentEntity extends AbstractExpandableItem implements MultiItemEntity {

    private String studentName;

    public StudentEntity(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public int getItemType() {
        return 5;
    }

    @Override
    public int getLevel() {
        return 5;
    }
}
