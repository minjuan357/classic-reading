package com.example.classicreading;

import com.example.classicreading.entity.Category;
import com.example.classicreading.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClassicReadingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassicReadingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(CategoryRepository categoryRepo) {
        return args -> {
            // 如果数据库中没有分类，才插入初始数据
            if (categoryRepo.count() == 0) {
                // 创建分类
                Category jing = new Category();
                jing.setName("经");
                jing.setDescription("儒家经典著作");
                categoryRepo.save(jing);

                Category shi = new Category();
                shi.setName("史");
                shi.setDescription("历史典籍");
                categoryRepo.save(shi);

                Category zi = new Category();
                zi.setName("子");
                zi.setDescription("诸子百家");
                categoryRepo.save(zi);

                Category ji = new Category();
                ji.setName("集");
                ji.setDescription("文学作品集");
                categoryRepo.save(ji);

                Category shici = new Category();
                shici.setName("诗词");
                shici.setDescription("唐诗宋词等");
                categoryRepo.save(shici);
            }

            // 外部 JSON 文件导入已禁用 — 这些文件在本地开发环境中不存在
            // 如需导入，请从 classpath resources/data/ 加载，或配置正确的文件路径
            System.out.println("应用启动完成，数据导入处理结束（外部导入已跳过）");
        };
    }
}