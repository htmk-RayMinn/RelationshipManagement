package com.ym.relationshipmanagement.controller;

import com.ym.relationshipmanagement.common.Result;
import com.ym.relationshipmanagement.entity.Contact;
import com.ym.relationshipmanagement.entity.ContactGroup;
import com.ym.relationshipmanagement.entity.Group;
import com.ym.relationshipmanagement.service.ContactGroupService;
import com.ym.relationshipmanagement.service.ContactService;
import com.ym.relationshipmanagement.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/api")
public class ContactGroupController {
    @Autowired
    private ContactGroupService contactGroupService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private GroupService groupService;

    //系统保护的分组名
    private static final String PROTECTED_GROUP_NAME = "全部";

    /**新增**/
    @PostMapping("/contacts/{contactId}/groups/{groupId}")
    public Result<String> addToGroup(@PathVariable Long contactId, @PathVariable Long groupId) {
        //判断联系人是否存在
        Contact contact = contactService.getById(contactId);
        if (contact == null) {
            return Result.error(404, "联系人不存在，id=" + contactId);
        }

        //判断分组是否存在
        Group group = groupService.getById(groupId);
        if (group == null) {
            return Result.error(404, "分组不存在，id=" + groupId);
        }

        //保护全部分组
        if (PROTECTED_GROUP_NAME.equals(group.getName())) {
            return Result.error(400, "【全部】是默认分组，所有联系人自动属于它，无需手动添加");
        }

        //检查是否在这个分组里
        ContactGroup existing = contactGroupService.lambdaQuery()
                .eq(ContactGroup::getGroupId,groupId)
                .eq(ContactGroup::getContactId,contactId)
                .one();
        if(existing != null){
            return Result.error(400, "该联系人已经在这个分组里了");
        }

        //建立关系
        ContactGroup relation = new ContactGroup();
        relation.setContactId(contactId);
        relation.setGroupId(groupId);

        boolean saved = contactGroupService.save(relation);
        if (saved) {
            return Result.ok("已将【" + contact.getName() + "】加入【" + group.getName() + "】");
        } else {
            return Result.error("加入分组失败，请重试");
        }

    }

    @DeleteMapping("/contacts/{contactId}/groups/{groupId}")
    public Result<String> removeFromGroup(
            @PathVariable Long contactId,
            @PathVariable Long groupId) {

        // 1. 先查关系是否存在
        ContactGroup existing = contactGroupService.lambdaQuery()
                .eq(ContactGroup::getContactId, contactId)
                .eq(ContactGroup::getGroupId, groupId)
                .one();
        if (existing == null) {
            return Result.error(404, "该联系人不在这个分组里");
        }

        // 2. 用条件构造器删除
        //    remove(wrapper) 根据条件删除，而不是根据主键删除
        boolean removed = contactGroupService.lambdaUpdate()
                .eq(ContactGroup::getContactId, contactId)
                .eq(ContactGroup::getGroupId, groupId)
                .remove();

        if (removed) {
            return Result.ok("已移出分组");
        } else {
            return Result.error("移出分组失败，请重试");
        }
    }

    // ============================================================
    //              查联系人属于哪些组
    // ============================================================

    /**
     * 查询某个联系人属于哪些分组
     *
     * GET /api/contacts/2/groups
     *
     * 思路：
     * 1. 查 contact_group 表，找到该联系人所有的 groupId
     * 2. 根据 groupId 列表，查 groups 表，返回 Group 列表
     */
    @GetMapping("/contacts/{contactId}/groups")
    public Result<List<Group>> getGroupsOfContact(@PathVariable Long contactId) {

        // 1. 校验联系人存在
        Contact existing = contactService.getById(contactId);
        if (existing == null){
            return Result.error(404,"该联系人不存在");
        }

        // 2. 查该联系人的所有关联记录，得到 groupId 列表
         List<ContactGroup> relations = contactGroupService.lambdaQuery()
                 .eq(ContactGroup::getContactId,contactId)
                 .list();
        // 3. 提取出所有 groupId
        List<Long> groupIds = new ArrayList<>();
        for (ContactGroup relation : relations){
            groupIds.add(relation.getGroupId());
        }
        // 4. 如果没有任何分组，直接返回空列表
        if (groupIds.isEmpty()){
            return Result.ok(new ArrayList<>());
        }

        // 5. 根据 groupId 批量查分组
        //    listByIds(集合) 是 IService 提供的批量查询方法
        List<Group> groups = groupService.listByIds(groupIds);
        return Result.ok(groups);
    }

    // ============================================================
    //              查某分组下有哪些联系人
    // ============================================================

    /**
     * 查询某个分组下的所有联系人
     *
     * GET /api/groups/3/contacts
     *
     * 思路同上，只是反过来：先查 contactId 列表，再查联系人
     */
    @GetMapping("/groups/{groupId}/contacts")
    public Result<List<Contact>> getContactsOfGroup(@PathVariable Long groupId) {

        // 1. 校验分组存在
         Group existing = groupService.getById(groupId);
         if (existing == null){
             return Result.error(404,"该分组不存在");
         }
        // 2. 查该分组的所有关联记录
        List<ContactGroup> relations = contactGroupService.lambdaQuery()
                .eq(ContactGroup::getGroupId,groupId)
                .list();
        // 3. 提取所有 contactId
        List<Long> contactIds = new ArrayList<>();
        for (ContactGroup relation : relations){
            contactIds.add(relation.getContactId());
        }
        // 4. 空列表直接返回
        if (contactIds.isEmpty()){
            return Result.ok(new ArrayList<>());
        }

        // 5. 批量查联系人
        List<Contact> contacts = contactService.listByIds(contactIds);
        return Result.ok(contacts);
    }


}
