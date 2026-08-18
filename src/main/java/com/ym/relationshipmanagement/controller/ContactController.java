package com.ym.relationshipmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ym.relationshipmanagement.common.Result;
import com.ym.relationshipmanagement.entity.Contact;
import com.ym.relationshipmanagement.entity.ContactAccount;
import com.ym.relationshipmanagement.service.ContactAccountService;
import com.ym.relationshipmanagement.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactAccountService contactAccountService;

    /**查询全部联系人**/
    @GetMapping
    public Result<List<Contact>> list() {

        // contactService.list() 是父类 IService 提供的，查全部记录
        List<Contact> contactList = contactService.list();

        // 即使列表为空（还没有联系人），也返回 200，data 为空数组 []
        return Result.ok(contactList);
    }
    //进入项目时或者刷新时，
    //创建一个名为list的方法，返回值类型时Result泛型是List泛型是Contact，把数据库每一行转成 Contact 对象，装进 List"
    //调用contactService继承的方法，将查到的联系人赋值给contactList变量，因为是整个联系人表，所以是List类型泛型是Contact
    //返回数据

    /**查询具体联系人**/
    @GetMapping("/{id}")
    public Result<Contact> getById(@PathVariable Long id) {

        // getById 来自 IService，按主键查询
        Contact contact = contactService.getById(id);

        if (contact == null) {
            return Result.error(404, "联系人不存在，id=" + id);
        }
        return Result.ok(contact);
    }
    //创建一个名为getById的方法，返回值类型是Result泛型是Contact
    //用@PathVariable获取url上的id值，赋值给Long类型的id变量
    //调用方法getByid，传入id这个变量，赋值给contact变量
    //判断如果contact为空，返回404，不然就是返回该联系人

    /**模糊查询联系人**/
    @GetMapping("/search")
    public Result<List<Contact>> search(@RequestParam String keyword) {

        // 调用我们自己写的 searchByName 方法（在 ContactServiceImpl 中实现的）
        List<Contact> contactList = contactService.searchByName(keyword);
        return Result.ok(contactList);
    }
    //创建一个名为search的方法，返回值类型是Result泛型是List泛型是Contact
    //这个@RequstParam注解是什么意思？传入的变量keyword，类型String。@RequestParam 就是取 URL 问号后面那个参数的值。
    //调用searchByName方法，传入keyword，赋值给contactList
    //返回ok方法


    /**新增联系人**/
    @PostMapping//新增用post
    public Result<Contact> create(@RequestBody Contact contact) {
        //判断新增的联系人名字是否为空或是空字符，又或是超过字数限制
        if (contact.getName() == null || contact.getName().isBlank()) {
            return Result.error("联系人姓名不能为空");
        }
        if (contact.getName().length() > 100) {
            return Result.error("联系人姓名超过限制");
        }
        //新增联系人名字符合规则，执行新增操作
        boolean saved = contactService.save(contact);
        if (saved) {
            return Result.ok(contact);
        } else {
            return Result.error("新增联系人失败，请稍后重试");
        }
    }

    //返回值是 Result<Contact> 类型，方法名是 create。
//@RequestBody 将JSON字符串转为Java对象。@ResponseBody将Java对象转为JSON但是@RestController自带。
//create方法里传入的参数为Contact这个对象类型
//先判断返回到的对象的name属性是否为空或是空字符串，这个||可以告诉我判断依据吗？我记得与是&&全真则真？
//在判断名字的长度是否超过限制
//调用contactService的save方法（contactService继承了IService，这个save方法是里面写好了的），传入对象，赋值给布尔类型的saved变量
//判断saved是否为真，真则调用Result的ok方法，传入对象，假为调用error

    /**删除联系人**/
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        //查看联系人是否存在
        Contact existing = contactService.getById(id);
        if (existing == null) {
            return Result.error(404, "联系人不存在，id=" + id);
        }
        //联系人存在，执行删除操作
        boolean removed = contactService.removeById(id);
        if (removed) {
            return Result.ok("删除成功，id=" + id + "，联系人：" + existing.getName());
        } else {
            return Result.error("删除失败，请稍后重试");
        }
    }
    //因为是删除，所以需要获取联系人的id值，所以将类型选为Delete，id值从url里获取
    //创建一个名为delete的方法，返回值类型是Result类型（自定义的类型），其中泛型为String，为什么是字符串类型？不像之前是实体类类型？新增：返回 Contact，因为前端可能需要用返回的数据（比如拿到新生成的 id）删除：返回 String，因为删完了就没了，返回一个提示文字就够了
    //类型是Long，变量名是id，不过前面这个@PathVariable注解，有什么用？@PathVariable 的作用：把 URL 路径中的占位符 {id} 的值取出来，赋给 id 变量。
    //创建一个Contact类型的变量：existing，用contactService继承的getById方法，传给existing，为什么这个existing类型是Contact？
    //判断，如果existing是null，不存在，那么返回联系人不存在
    //创建一个布尔类型的变量：removed，调用contactService继承的方法removeById，传给这个变量
    //判断，如果变量存在，则删除成功，不存在则删除失败

    @PutMapping("/{id}")
    public Result<Contact> update(@PathVariable Long id,@RequestBody Contact contact){
        Contact existing = contactService.getById(id);
        if (existing == null){
            return Result.error(404,"联系人不存在，id="+id);
        }

        if (contact.getName() != null){
            existing.setName(contact.getName());
        }
        if (contact.getBirthDate() != null){
            existing.setBirthDate(contact.getBirthDate());
        }
        if (contact.getGender() != null){
            existing.setGender(contact.getGender());
        }
        if (contact.getAddress() != null) {
            existing.setAddress(contact.getAddress());
        }
        if (contact.getNotes() != null) {
            existing.setNotes(contact.getNotes());
        }

        existing.setUpdatedAt(null);

        boolean updated = contactService.updateById(existing);

        if (updated) {
            Contact latest = contactService.getById(id);
            return Result.ok(latest);
        } else {
            return Result.error("更新失败，请稍后重试");
        }
    }
    //创建一个名为update的方法，返回值类型是Result，泛型是Contact。
//传参中，变量id因为@PathVariable获取到了url的id值，contact变量因为@RequestBody将前端返回的JSON（修改的内容）转为实体类型。
//先查要修改的联系人是否存在。
//判断，前端传回的数据中，字段为空吗？不为空就覆盖原有的字段
//existing这个变量里被赋值的相当于是前端传回的数据（用户对联系人修改后的数据，有些改了，就有数据，有些没改，就无数据）。再将existing传入updateById这个方法。赋值给updated变量。existing 不是"被赋值成前端数据"，而是旧数据 + 新修改的合并体：所以 existing = 以旧数据为底稿，只把前端传了的字段覆盖上去。不是直接拿前端数据替代。
//如果updated存在，就要调用ok，不存在就error。
    //我还有一点想问的是，我注意到这里的代码可以修改的实体类属性，每个属性都被判断了一遍，如果是属性很多有几十个，难道也傻傻的一个一个判断？

    /**进入联系人详情页时，展示该联系人的所有平台账号**/
    @GetMapping("/{contactId}/accounts")
    public  Result<List<ContactAccount>> getAccounts(@PathVariable Long contactId){
        // 先确认联系人存在
        Contact contact = contactService.getById(contactId);
        if (contact == null) {
            return Result.error(404, "联系人不存在，id=" + contactId);
        }

        LambdaQueryWrapper<ContactAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContactAccount::getContactId,contactId);
        wrapper.orderByDesc(ContactAccount::getCreatedAt);

        List<ContactAccount> accounts = contactAccountService.list(wrapper);
        return Result.ok(accounts);
    }
}

