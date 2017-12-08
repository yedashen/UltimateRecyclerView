package shen.da.ye.ultimaterecyclerview.util;

import java.util.ArrayList;

import shen.da.ye.ultimaterecyclerview.entity.ClassEntity;
import shen.da.ye.ultimaterecyclerview.entity.CollegeEntity;
import shen.da.ye.ultimaterecyclerview.entity.MultiItemEntity;
import shen.da.ye.ultimaterecyclerview.entity.ProfessionEntity;
import shen.da.ye.ultimaterecyclerview.entity.SchoolEntity;
import shen.da.ye.ultimaterecyclerview.entity.StudentEntity;

/**
 * @author ChenYe
 *         created by on 2017/12/8 0008. 09:12
 **/

public class CreateDataUtil {

    /**
     * 我特地把这个抽取，不想用for循环来造数据
     */
    public static ArrayList<MultiItemEntity> generateExpandData() {
        ArrayList<MultiItemEntity> datas = new ArrayList<>();
        SchoolEntity PekingEntity = new SchoolEntity("北京大学");

        CollegeEntity bdMath = new CollegeEntity("北京大学数学科学学院");
        ProfessionEntity dbZy1 = new ProfessionEntity("光学数学专业");
        ClassEntity gx1 = new ClassEntity("北大数学科学院光学数学一班");
        StudentEntity gx1Student1 = new StudentEntity("赵云");
        StudentEntity gx1Student2 = new StudentEntity("赵子龙");
        StudentEntity gx1Student3 = new StudentEntity("小赵");
        StudentEntity gx1Student4 = new StudentEntity("小云");
        gx1.addSubItem(gx1Student1);
        gx1.addSubItem(gx1Student2);
        gx1.addSubItem(gx1Student3);
        gx1.addSubItem(gx1Student4);
        dbZy1.addSubItem(gx1);


        ClassEntity gx2 = new ClassEntity("北大数学科学院光学数学二班");
        StudentEntity gx2Student1 = new StudentEntity("李白");
        StudentEntity gx2Student2 = new StudentEntity("高渐离");
        StudentEntity gx2Student3 = new StudentEntity("陈近南");
        StudentEntity gx2Student4 = new StudentEntity("曹雪芹");
        StudentEntity gx2Student5 = new StudentEntity("李伟");
        StudentEntity gx2Student6 = new StudentEntity("范文程");
        StudentEntity gx2Student7 = new StudentEntity("刘墉");
        StudentEntity gx2Student8 = new StudentEntity("郑板桥");
        gx2.addSubItem(gx2Student1);
        gx2.addSubItem(gx2Student2);
        gx2.addSubItem(gx2Student3);
        gx2.addSubItem(gx2Student4);
        gx2.addSubItem(gx2Student5);
        gx2.addSubItem(gx2Student6);
        gx2.addSubItem(gx2Student7);
        gx2.addSubItem(gx2Student8);
        dbZy1.addSubItem(gx2);

        ClassEntity gx3 = new ClassEntity("北大数学科学院光学数学三班");
        StudentEntity gx3Student1 = new StudentEntity("年羹尧");
        StudentEntity gx3Student2 = new StudentEntity("林则徐");
        gx3.addSubItem(gx3Student1);
        gx3.addSubItem(gx3Student2);
        dbZy1.addSubItem(gx3);

        ClassEntity gx4 = new ClassEntity("北大数学科学院光学数学四班");
        StudentEntity gx4Student1 = new StudentEntity("周培公");
        StudentEntity gx4Student2 = new StudentEntity("索尼");
        StudentEntity gx4Student3 = new StudentEntity("年明珠");
        gx4.addSubItem(gx4Student1);
        gx4.addSubItem(gx4Student2);
        gx4.addSubItem(gx4Student3);
        dbZy1.addSubItem(gx4);

        bdMath.addSubItem(dbZy1);

        ProfessionEntity dbZy2 = new ProfessionEntity("计算机数学专业");
        ClassEntity itClass1 = new ClassEntity("北大计算机数学专业一班");
        StudentEntity itClass1_1 = new StudentEntity("马云");
        StudentEntity itClass1_2 = new StudentEntity("李彦宏");
        StudentEntity itClass1_3 = new StudentEntity("马化腾");
        itClass1.addSubItem(itClass1_1);
        itClass1.addSubItem(itClass1_2);
        itClass1.addSubItem(itClass1_3);
        dbZy2.addSubItem(itClass1);

        ClassEntity itClass2 = new ClassEntity("北大计算机数学专业二班");
        StudentEntity itClass2_1 = new StudentEntity("欧阳锋");
        StudentEntity itClass2_2 = new StudentEntity("张衡");
        itClass1.addSubItem(itClass2_1);
        itClass1.addSubItem(itClass2_2);
        dbZy2.addSubItem(itClass2);

        ClassEntity itClass3 = new ClassEntity("北大计算机数学专业三班");
        StudentEntity itClass3_1 = new StudentEntity("刘备");
        StudentEntity itClass3_2 = new StudentEntity("曹操");
        StudentEntity itClass3_3 = new StudentEntity("张瑜");
        itClass1.addSubItem(itClass3_1);
        itClass1.addSubItem(itClass3_2);
        itClass1.addSubItem(itClass3_3);
        dbZy2.addSubItem(itClass3);

        bdMath.addSubItem(dbZy2);


        CollegeEntity bdChineseCollege = new CollegeEntity("北京大学人文科学学院");
        ProfessionEntity ls = new ProfessionEntity("北京大学人文科学学院历史文学专业");
        ClassEntity lsClass_1 = new ClassEntity("历史文学专业一班");
        StudentEntity lsClass_1_1 = new StudentEntity("干将");
        StudentEntity lsClass_1_2 = new StudentEntity("洪成涛");
        lsClass_1.addSubItem(lsClass_1_1);
        lsClass_1.addSubItem(lsClass_1_2);
        ls.addSubItem(lsClass_1);
        bdChineseCollege.addSubItem(ls);

        PekingEntity.addSubItem(bdMath);
        PekingEntity.addSubItem(bdChineseCollege);

        datas.add(PekingEntity);
        return datas;
    }
}
