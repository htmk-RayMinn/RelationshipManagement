package com.ym.relationshipmanagement.common;

import lombok.Data;

/**
 * 统一响应结果类
 *
 * 所有接口都用这个类包裹返回数据，前端可以统一判断：
 *   - code == 200  → 成功
 *   - code != 200  → 失败，看 message 获取原因
 *
 * 泛型 <T> 表示 data 可以是任意类型：
 *   - Result<Contact>        → 单个联系人
 *   - Result<List<Contact>>  → 联系人列表
 *   - Result<String>         → 提示文字
 *
 * 使用示例：
 *   return Result.ok(contact);               // 成功，带数据
 *   return Result.ok();                      // 成功，无数据（如删除成功）
 *   return Result.error("联系人不存在");       // 失败，带错误信息
 */
@Data
public class Result<T> {

    /** 状态码，200 表示成功，其他表示失败 */
    private int code;

    /** 提示信息，成功或失败的原因 */
    private String message;

    /** 返回的数据，泛型，可以是对象、列表、null */
    private T data;

    // ==================== 静态工厂方法，方便快速构建 Result ====================

    /**
     * 成功 — 带数据
     * 用法：return Result.ok(contact);
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "成功";
        result.data = data;
        return result;
    }

    /**
     * 成功 — 不带数据（如删除/更新操作）
     * 用法：return Result.ok();
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 失败 — 自定义错误信息
     * 用法：return Result.error("联系人不存在");
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.code = 500;          // 默认用 500，你也可以改成其他错误码
        result.message = message;
        result.data = null;
        return result;
    }

    /**
     * 失败 — 自定义状态码 + 错误信息
     * 用法：return Result.error(404, "联系人不存在");
     */
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.data = null;
        return result;
    }
}
