const $video = document.getElementById('video');
const $canvas = document.getElementById('canvas');
const $cnt = document.getElementById('badPostCnt');

const width = 700;
const height = 500;
var todayBadPose = 0; //오늘의 나쁜 자세 총횟수
var cntBadPose = 0; //나쁜 자세 연속횟수
var audio = document.getElementById('audio');

function predict(data)
{
    if (data == 0) //바른 자세 X
    {
        cntBadPose++;
    }
    else{
        cntBadPose = 0
    }
    if (cntBadPose == 3)
    {
        audio.play();
        todayBadPose++;
        $cnt.innerHTML = todayBadPose;
        cntBadPose = 0;
    }
}

/**
 * 비디오 이미지 캡쳐
 */
function capture() {

    const context = $canvas.getContext('2d');
    context.drawImage($video, 0, 0, width, height);
    const imgBase64 = $canvas.toDataURL('image/jpeg', 'image/octet-stream');
    const decodImg = atob(imgBase64.split(',')[1]);

    let array = [];
    for (let i = 0; i < decodImg .length; i++) {
        array.push(decodImg .charCodeAt(i));
    }

    const file = new Blob([new Uint8Array(array)], {type: 'image/jpeg'});
    const fileName = 'canvas_img_' + new Date().getMilliseconds() + '.jpg';
    let formData = new FormData();
    formData.append('file', file, fileName);

    var predictResult;

    $.ajax({
        type: 'post',
        url: '/detectImg',
        cache: false,
        data: formData,
        processData: false,
        contentType: false,
        async: false,
        success: function(data)
        {
            console.log("todayBadPose : ",todayBadPose," cntBadPose : ",cntBadPose);
            predictResult = data;
        },
        error(data){
            console.log("data : ",data);
            console.log("todayBadPose : ",todayBadPose);
            console.log("cntBadPose : ",cntBadPose);
        }
    })
    predict(predictResult);
}

/**
 * getUserMedia 성공
 * @param stream
 */
function success(stream) {
    $video.srcObject = stream;
}

/**
 * getUserMedia 실패
 * @param err
 */
function error(err) {
    console.log('error', err);
    alert(err.message);
}

/**
 * 미디어 호출
 */
async function startMedia() {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({
            audio: false,
            video: true,
        });
        success(stream);
    } catch (err) {
        error(err);
    }
}

/**
 * 초기 이벤트 바인딩
 */
function initialize() {
    $canvas.width = width;
    $canvas.height = height;
    $cnt.innerHTML = todayBadPose;
    startMedia();
}

initialize();
setInterval(capture, 1000);