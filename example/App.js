import React, { Component } from 'react';
import { Platform, StyleSheet, Image, Text, View, Dimensions, Button, TouchableOpacity, NativeModules, NativeEventEmitter } from 'react-native';
import { JMRTMPMonitorView } from 'react-native-rtmp-player-jm';
import RNFS from 'react-native-fs';
const { height, width } = Dimensions.get('window');
const {
    JMRTMPPlayerManager
} = NativeModules;

export default class App extends Component<Props> {
    state = {
        rtmpManagerListener: new NativeEventEmitter(JMRTMPPlayerManager),
        playStatusSubscription: null,
        talkStatusSubscription: null,
        recordStatusSubscription: null,
        frameInfoSubscription: null,
        receiveDeviceSubscription: null,
        mute: false
    };

    componentWillMount() {
        playStatusSubscription = this.state.rtmpManagerListener.addListener(JMRTMPPlayerManager.kOnStreamPlayerPlayStatus, (reminder) => {
            console.log(reminder);
            if (reminder.status == JMRTMPPlayerManager.videoStatusPrepare) {
                console.log("准备播放视频");
            } else if (reminder.status == JMRTMPPlayerManager.videoStatusStart) {
                console.log("视频已开始播放");
            } else if (reminder.status == JMRTMPPlayerManager.videoStatusStop) {
                console.log("视频已停止播放");
            } else {
                console.log("视频播放失败，Code:" + reminder.errCode + ",ErrMag:" + reminder.errMsg);
            }

        });
        talkStatusSubscription = this.state.rtmpManagerListener.addListener(JMRTMPPlayerManager.kOnStreamPlayerTalkStatus, (reminder) => { console.log(reminder); });
        recordStatusSubscription = this.state.rtmpManagerListener.addListener(JMRTMPPlayerManager.kOnStreamPlayerRecordStatus, (reminder) => { console.log(reminder); });
        frameInfoSubscription = this.state.rtmpManagerListener.addListener(JMRTMPPlayerManager.kOnStreamPlayerReceiveFrameInfo, (reminder) => { //console.log(reminder);
        });
        receiveDeviceSubscription = this.state.rtmpManagerListener.addListener(JMRTMPPlayerManager.kOnStreamPlayerReceiveDeviceData, (reminder) => { console.log(reminder); });
    }

    componentWillUnmount() {
        if (playStatusSubscription) playStatusSubscription.remove();
        if (talkStatusSubscription) talkStatusSubscription.remove();
        if (recordStatusSubscription) recordStatusSubscription.remove();
        if (frameInfoSubscription) frameInfoSubscription.remove();
        if (receiveDeviceSubscription) receiveDeviceSubscription.remove();
    }


    render() {
        return (
            <View style={{ flex: 1, backgroundColor: '#FFF' }}>
                <JMRTMPMonitorView
                    style={{ width: width, height: 300}}
                    image={require('./res/image/screenShot.png')}
                >
                </JMRTMPMonitorView>

                <View style={{ flexDirection: 'row', justifyContent: 'space-between', width: width, height: 40, marginTop: 10 }}>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedInitSDK() }}>
                        <Text style={styles.baseStyle}>初始化SDK</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedStartPlay() }}>
                        <Text style={styles.baseStyle}>开始</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedStopPlay() }}>
                        <Text style={styles.baseStyle}>停止</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedStartPlayback() }}>
                        <Text style={styles.baseStyle}>回放</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedStartRecording() }}>
                        <Text style={styles.baseStyle}>开始录制</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedStopRecording() }}>
                        <Text style={styles.baseStyle}>停止录制</Text>
                    </TouchableOpacity>
                </View>

                <View style={{ flexDirection: 'row', justifyContent: 'space-between', width: width, height: 40, marginTop: 10 }}>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedReleaseSDK() }}>
                        <Text style={styles.baseStyle}>释放SDK</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedStartTalk() }}>
                        <Text style={styles.baseStyle}>开始对讲</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedInitSDK() }}>
                        <Text style={styles.baseStyle}>停止对讲</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedSendCustomRequest() }}>
                        <Text style={styles.baseStyle}>发送消息</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedSnapshot() }}>
                        <Text style={styles.baseStyle}>拍照</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedMute() }}>
                        <Text style={styles.baseStyle}>静音</Text>
                    </TouchableOpacity>
                </View>

                <View style={{ flexDirection: 'row', justifyContent: 'space-between', width: width, height: 40, marginTop: 10 }}>
                    <TouchableOpacity style={styles.btn} onPress={() => { this.clickedSwitchCamera() }}>
                        <Text style={styles.baseStyle}>切换摄像头</Text>
                    </TouchableOpacity>
                </View>
            </View>
        );
    }

    clickedInitSDK() {
//        JMRTMPPlayerManager.initialize("cd15d1aba85346128811ae17fc2a2378", "a7866ef45d594ea988554fe633fa987e", "983135884798102")
        JMRTMPPlayerManager.initialize("69dcc204c82e4861a7a763c6bb3f4b96", "fcb0f7e8ec9e4ed89d632240f4e1b8b9", "357730090535536")
    }

    clickedReleaseSDK() {
        JMRTMPPlayerManager.deInitialize()
    }

    clickedStartPlay() {
        JMRTMPPlayerManager.startPlayLive()
    }

    clickedStartPlayback() {
        JMRTMPPlayerManager.startPlayback(["2018-08-29-09-21-27_30268.mp4", "2018-08-29-09-25-59_35532.mp4", "2018-08-29-09-59-07_65784.mp4"])
    }

    clickedStopPlay() {
        JMRTMPPlayerManager.stopPlay()
    }

    clickedStartRecording() {
        const path = Platform.OS === 'android' ? (RNFS.ExternalStorageDirectoryPath + "/") : RNFS.TemporaryDirectoryPath;
        console.log(path);
        JMRTMPPlayerManager.startRecording(path + "123.mp4")    //需要具体的地址，比如:"/Users/lzj/Library/Developer/CoreSimulator/Devices/021EF370-EB9F-4D6D-808C-90D44B213009/data/Containers/Data/Application/307AB34A-652E-4F56-AE02-B7C82A75F3DB/tmp/123.mp4"
    }

    clickedStopRecording() {
        JMRTMPPlayerManager.stopRecording()
    }

    clickedStartTalk() {
        JMRTMPPlayerManager.startTalk()
    }

    clickedStopTalk() {
        JMRTMPPlayerManager.stopTalk()
    }

    clickedSendCustomRequest() {
        JMRTMPPlayerManager.sendCustomRequest({ "test": "123" }).then(data => {
            console.log(data);
        }).catch(e => {
            console.log(e);
        });
    }

    clickedSnapshot() {
        //需要具体的地址，比如:"/Users/lzj/Library/Developer/CoreSimulator/Devices/021EF370-EB9F-4D6D-808C-90D44B213009/data/Containers/Data/Application/307AB34A-652E-4F56-AE02-B7C82A75F3DB/tmp/123.png"
        const path = Platform.OS === 'android' ? (RNFS.ExternalStorageDirectoryPath + "/") : RNFS.TemporaryDirectoryPath;
        console.log(path);
        JMRTMPPlayerManager.snapshot(path + "123.png").then(data => {
            console.log(data);
        }).catch(e => {
            console.log(e);
        });
    }

    clickedSwitchCamera() {
        JMRTMPPlayerManager.switchCamera(false, true).then(data => {
            console.log(data);
        }).catch(e => {
            console.log(e);
        });
    }

    clickedStopAll() {
        JMRTMPPlayerManager.stop();
    }

    clickedMute() {
        this.setState({ mute: !this.state.mute }, () => {
            JMRTMPPlayerManager.setMute(this.state.mute);
        });
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },

    btn: {
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#AAFCFF',
        borderColor: '#ca6',
        borderWidth: 1,
        width: (width - 15) / 6
    }
});
