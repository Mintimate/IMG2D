import COS from "cos-js-sdk-v5"

const bucket = 'image-file-host-1302972711';
const region = 'ap-shanghai';
const prefix = 'https://image-file-host-1302972711.cos.ap-shanghai.myqcloud.com/img2dPost/'

export function uploadFile(file, fileName) {

  let cos = new COS({
    SecretId: 'AKIDPZSxBI2h281fNtTXVEguswlZ8PRC7Zv1',
    SecretKey: 'KODl4TbU8YjicGnNhxykHWXIFuMkyYwp',
    ProgressInterval: 1000 // 控制上传的 onProgress 回调的间隔
  });
  cos.putObject({
    Bucket: bucket,
    Region: region,
    Key: 'img2dPost/' + fileName,
    StorageClass: 'STANDARD',
    Body: file, // 上传文件对象
    onProgress: progressData => {
      console.log(JSON.stringify(progressData))
    }
  }, function (err) {
    console.log('err=' + JSON.stringify(err));
    // console.log('data=' + JSON.stringify(data));
    //let path = 'filename + '';
    //console.log('path='+path);
  });
  return prefix + fileName
}
