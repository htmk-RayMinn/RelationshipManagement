package com.ym.relationshipmanagement.controller;

import com.ym.relationshipmanagement.common.Result;
import com.ym.relationshipmanagement.entity.Contact;
import com.ym.relationshipmanagement.entity.ContactAccount;
import com.ym.relationshipmanagement.service.ContactAccountService;
import com.ym.relationshipmanagement.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class ContactAccountController {

    @Autowired
    private ContactAccountService contactAccountService;


    /**查询所有联系人的所有账号**/
    @GetMapping
    public Result<List<ContactAccount>> list(){
        List<ContactAccount> contactAccountList = contactAccountService.list();
        return Result.ok(contactAccountList);
    }

    /**模糊查询联系人账号**/
    @GetMapping("/search")
    public Result<List<ContactAccount>> search(@RequestParam String keyword){
        List<ContactAccount> contactAccountList = contactAccountService.searchByKeyword(keyword);
        return Result.ok(contactAccountList);
    }

    /**查单个账号**/
    @GetMapping("/{id}")
    public Result<ContactAccount> getById(@PathVariable Long id){
        ContactAccount contactAccount = contactAccountService.getById(id);
        if (contactAccount == null){
            return Result.error(404,"账号不存在，id="+id);
        }
        return Result.ok(contactAccount);
    }

    /**新建联系人账号**/
    @PostMapping
    public Result<ContactAccount> create(@RequestBody ContactAccount contactAccount) {

        // 校验 contactId
        if (contactAccount.getContactId() == null) {
            return Result.error(400, "所属联系人ID不能为空");
        }

        // 校验平台名
        if (contactAccount.getPlatformName() == null
                || contactAccount.getPlatformName().isBlank()) {
            return Result.error(400, "平台名不能为空");
        }
        if (contactAccount.getPlatformName().length() > 50) {
            return Result.error(400, "平台名不能超过50个字符");
        }

        // 校验账号
        if (contactAccount.getAccount() == null
                || contactAccount.getAccount().isBlank()) {
            return Result.error(400, "账号不能为空");
        }
        if (contactAccount.getAccount().length() > 200) {
            return Result.error(400, "账号不能超过200个字符");
        }

        boolean saved = contactAccountService.save(contactAccount);
        if (saved) {
            return Result.ok(contactAccount);
        } else {
            return Result.error("新增失败，请重试");
        }
    }

    /**更新联系人账号信息**/
    @PutMapping("/{accountId}")
    public Result<ContactAccount> update(@RequestBody ContactAccount contactAccount,@PathVariable Long accountId){
        ContactAccount existing = contactAccountService.getById(accountId);
        if (existing == null){
            return Result.error(404,"账号不存在，id="+accountId);
        }

        if (contactAccount.getPlatformName() != null && !contactAccount.getPlatformName().isBlank()){
            existing.setPlatformName(contactAccount.getPlatformName());
        }
        if (contactAccount.getAccount() != null && !contactAccount.getAccount().isBlank()){
            existing.setAccount(contactAccount.getAccount());
        }
        if (contactAccount.getNotes() != null) {
            existing.setNotes(contactAccount.getNotes());    // notes 允许清空，不检查 isBlank
        }
        if (contactAccount.getIsPrimary() != null) {
            existing.setIsPrimary(contactAccount.getIsPrimary());
        }

        existing.setUpdatedAt(null);

        boolean updated = contactAccountService.updateById(existing);
        if (updated){
            ContactAccount latest = contactAccountService.getById(accountId);
            return Result.ok(latest);
        }else {
            return Result.error("更新失败，稍后重试");
        }
    }

    /** 删除账号 */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        ContactAccount existing = contactAccountService.getById(id);
        if (existing == null) {
            return Result.error(404, "账号不存在，id=" + id);
        }

        boolean removed = contactAccountService.removeById(id);
        if (removed) {
            return Result.ok("删除成功，id=" + id);
        } else {
            return Result.error("删除失败，请重试");
        }
    }
}
