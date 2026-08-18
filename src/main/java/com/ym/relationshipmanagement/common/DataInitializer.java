package com.ym.relationshipmanagement.common;

import com.ym.relationshipmanagement.entity.Group;
import com.ym.relationshipmanagement.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    //CommandLineRunner 是一个"启动后自动执行"的接口。
    @Autowired
    private GroupService groupService;

    @Override
    public void run(String... args){
        //这是 Java 的可变参数语法。String... 表示"可以接收 0 个或多个 String"。
        // ==================== 初始化"全部"分组 ====================
        // 先查一下数据库里有没有名为"全部"的分组
        // 有就不插入（避免每次启动都重复插入），没有就创建

        Group allGroup = groupService.lambdaQuery()
                .eq(Group::getName, "全部")
                .one();   // .one() 查一条记录，没有则返回 null

        if (allGroup == null) {
            Group group = new Group();
            group.setName("全部");
            group.setDescription("系统默认分组，所有联系人都属于此分组");
            groupService.save(group);
            System.out.println("✅ 已创建默认分组：【全部】");
        } else {
            System.out.println("✅ 默认分组【全部】已存在，跳过");
        }
    }
}
