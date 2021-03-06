<script type="text/javascript">

    // 处理添加
    function queue_handle_add(dig) {

        parent.$.messager.progress({ // 打开进程对话框
            text: '正在执行，请稍后....',
            interval: 100
        });

        var addForm = $('#queue_add_form');
        if (!addForm.form('validate')) {
            parent.$.messager.progress('close'); // 关闭进程对话框
            return;
        }

        addForm.form('submit', {
            url: 'queue',
            success: function (retJson) {
                parent.$.messager.progress('close');  // 关闭进程对话框
                var retObj = $.parseJSON(retJson);
                if (retObj.code === 0) {
                    $('#queue_datagrid').datagrid('load'); // 动态刷新
                    dig.dialog('close'); // 关闭窗口
                    $.messager.show({title: '成功提示', msg: retObj.msg});
                } else {
                    $.messager.alert('错误提示', retObj.msg, 'error');
                }
            }
        });


    }
</script>

<form id="queue_add_form" method="post" class="field-form">
    <table>
        <tr>
            <td>业务名称<span class="span_required">*</span></td>
            <td>
                <input name="businessName" class="easyui-validatebox" maxlength="32" style="width: 165px;"
                       data-options="
                       required: true,
                       missingMessage: '请填写业务名称'
                       "/>
            </td>
            <td>消费队列<span class="span_required">*</span></td>
            <td>
                <input name="consumerQueue" class="easyui-validatebox" maxlength="64" style="width: 165px;"
                       data-options="
                       required: true,
                       missingMessage: '请填写消费队列'
                       "/>
            </td>
        </tr>
        <tr>
            <td>消息确认url<span class="span_required">*</span></td>
            <td colspan="3">
                <input name="checkUrl" class="easyui-validatebox" maxlength="256" style="width: 468px;"
                       data-options="
                       required: true,
                       missingMessage: '请填写消息确认url'
                       "/>
                </br>
                注：确认消息返回规范</br>
                {</br>
                &nbsp;&nbsp;"code":0,</br>
                &nbsp;&nbsp;"data":1 </br>
                }
                </br>
                code: 0 成功 1 失败 </br>
                data: 0 业务处理失败，删除半提交消息 1 业务处理成功，RMQ发送半消息到MQ中间件 2 业务处理成功，RMQ删除半提交消息 </br>
            </td>
        </tr>
        <tr>
            <td>确认条件(毫秒)<span class="span_required">*</span></td>
            <td colspan="3">
                <input name="checkDuration" class="easyui-numberbox" style="width: 165px;"
                       data-options="
                       required: true,
                       missingMessage: '请填写确认确认条件',
                       min: 1,
                       max: 99999999
                       "/>
                注：多长时间未确认的消息需进行确认
            </td>
        </tr>
        <tr>
            <td>确认超时时长(毫秒)<span class="span_required">*</span></td>
            <td colspan="3">
                <input name="checkTimeout" class="easyui-numberbox" style="width: 165px;"
                       data-options="
                       required: true,
                       missingMessage: '请填写确认超时时长',
                       min: 1,
                       max: 5000
                       "/>
                注：HTTP请求超时时长
            </td>
        </tr>
    </table>
</form>
