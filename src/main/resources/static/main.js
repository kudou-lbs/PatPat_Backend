//防止文件为空或体积过大
function checkImg(){
    var fileName=$("#filename").val()
    fileName=fileName.replace("","")
    var flag=true
    if(fileName==""){
        flag=false
        alert("请选择图片")
    }
    else{
        var size = $("#filename")[0].files[0].size
        if(size/10000000>100){
            flag=false
            alert("图片大小不能超过100KB")
        }
    }
    if(flag){
        showLoadImg()
    }
    else{
        $("#filename").val("")
    }
    return flag
}

//图片预览
function showLoadImg(){
    var file=$('#filename').get(0).files[0]
    var reader = new FileReader()

    //将文件以Data URL形式读入页面
    reader.readAsDataURL(file)
    console.log(file.toString())
    reader.onload = function(e){
        //显示文件
        $("#load_img").html('<p>上传图片预览</p>' +
                            '<img src="' + this.result +'" alt="" />');
    }
}

//图片上传
function imgUpload(){
    //formdata形式数据阐述
    var formdata=new FormData();
    formdata.append('fileName', $('#filename').get(0).files[0]);
    formdata.append('nickname', $('#nickname').val())
    console.log($('#nickname').val())

    //ajax上传
    $.ajax({
        async: false,
        type: 'POST',
        url: "/test/uploadImg",
        dataType: 'json',
        data: formdata,
        contentType:false,//ajax上传图片需要添加
        processData:false,//ajax上传图片需要添加
        success: function (data) {
            console.log(data)
            var result = data.result
            $("#message").html(result)
        },
        error: function (e) {
            alert("error,请输入正确参数");
        }
    })
}

//根据所有者显示图像
function showImg(){
    //获取owner
    var nickname = $('#nickname').val()

    //formdata形式数据阐述
    var formdata=new FormData();
    formdata.append('nickname', $('#nickname').val())
    console.log($('#nickname').val())

    //ajax获取图像url
    $.ajax({
        async: false,
        type: 'POST',
        url: "/test/getImgPath",
        dataType: 'json',
        data: formdata,
        contentType:false,//ajax上传图片需要添加
        processData:false,//ajax上传图片需要添加
        success: function (data) {
            console.log(data)
            var result = data.paths
            var str_img = ""
            if(result.length == 0){
                $("#message").html("该用户无图")
            }
            else {
                result.forEach(function (item, index, array) {
                    str_img += '<img src=' + item + '"/" alt="" />' + "</br>"
                })
                console.log(str_img)
                $("#owner_img").html(str_img)
            }
        },
        error: function (e) {
            alert("error");
        }
    })
}