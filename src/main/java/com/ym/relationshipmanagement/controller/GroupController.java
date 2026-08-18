package com.ym.relationshipmanagement.controller;

import com.ym.relationshipmanagement.common.Result;
import com.ym.relationshipmanagement.entity.Group;
import com.ym.relationshipmanagement.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 系统保护的分组名（不能被删除和修改）
     */
    private static final String PROTECTED_GROUP_NAME = "全部";

    // ==================== 查 ====================

    /** 查询所有分组 */
    @GetMapping
    public Result<List<Group>> list() {
        List<Group> groupList = groupService.list();
        return Result.ok(groupList);
    }

    /** 查询单个分组 */
    @GetMapping("/{id}")
    public Result<Group> getById(@PathVariable Long id) {
        Group group = groupService.getById(id);
        if (group == null) {
            return Result.error(404, "分组不存在，id=" + id);
        }
        return Result.ok(group);
    }

    // ==================== 增 ====================

    /** 新增分组 */
    @PostMapping
    public Result<Group> create(@RequestBody Group group) {

        // 1. 校验组名
        if (group.getName() == null || group.getName().isBlank()) {
            return Result.error(400, "分组名字不能为空");
        }
        if (group.getName().length() > 50) {
            return Result.error(400, "分组名不能超过50个字符");
        }

        // 2. 禁止创建名为"全部"的分组（系统保留）
        if (PROTECTED_GROUP_NAME.equals(group.getName())) {
            return Result.error(400, "【全部】是系统保留分组名，不能使用");
        }

        // 3. 检查是否已存在同名分组
        Group existing = groupService.lambdaQuery()
                .eq(Group::getName, group.getName())
                .one();
        if (existing != null) {
            return Result.error(400, "分组名已存在：" + group.getName());
        }

        // 4. 执行新增
        boolean saved = groupService.save(group);
        if (saved) {
            return Result.ok(group);
        } else {
            return Result.error("新增分组失败，请重试");
        }
    }

    // ==================== 改 ====================

    /** 更新分组 */
    @PutMapping("/{id}")
    public Result<Group> update(@PathVariable Long id, @RequestBody Group group) {

        // 1. 查出旧数据
        Group existing = groupService.getById(id);
        if (existing == null) {
            return Result.error(404, "分组不存在，id=" + id);
        }

        // 2. 保护"全部"组
        if (PROTECTED_GROUP_NAME.equals(existing.getName())) {
            return Result.error(400, "【全部】是系统默认分组，不能修改");
        }

        // 3. 覆盖字段
        if (group.getName() != null && !group.getName().isBlank()) {
            // 确实改名了才检查重名
            if (!group.getName().equals(existing.getName())) {
                Group conflict = groupService.lambdaQuery()
                        .eq(Group::getName, group.getName())
                        .one();
                if (conflict != null) {
                    return Result.error(400, "分组名已存在：" + group.getName());
                }
            }
            existing.setName(group.getName());
        }
        if (group.getDescription() != null) {
            existing.setDescription(group.getDescription());
        }

        // 4. 执行更新
        boolean updated = groupService.updateById(existing);
        if (updated) {
            Group latest = groupService.getById(id);
            return Result.ok(latest);
        } else {
            return Result.error("更新分组失败，请重试");
        }
    }

    // ==================== 删 ====================

    /** 删除分组 */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {

        // 1. 查出要删的分组
        Group existing = groupService.getById(id);
        if (existing == null) {
            return Result.error(404, "分组不存在，id=" + id);
        }

        // 2. 保护"全部"组
        if (PROTECTED_GROUP_NAME.equals(existing.getName())) {
            return Result.error(400, "【全部】是系统默认分组，不能删除");
        }

        // 3. 执行删除
        boolean removed = groupService.removeById(id);
        if (removed) {
            return Result.ok("删除成功，分组：" + existing.getName());
        } else {
            return Result.error("删除分组失败，请重试");
        }
    }
}
