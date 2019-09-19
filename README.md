
# react-native-baidu-map-jm [![npm version](https://img.shields.io/npm/v/react-native-baidu-map-jm.svg?style=flat)](https://www.npmjs.com/package/react-native-baidu-map-jm)

- 此版本为lovebing的分支仓库，特此感谢作者的开源。因项目需求，iOS版百度最新的SDK作者未实现Overlay，个人在其基础上参考Android进行修改和完善。

Baidu Map SDK modules and view for React Native(Android & IOS), support react native 0.57+

百度地图 React Native 模块，支持 react native 0.57+，已更新到最新的百度地图SDK版本。

![IOS](https://raw.githubusercontent.com/Eafy/react-native-baidu-map-jm/master/images/ios.jpg)
![Android](https://raw.githubusercontent.com/Eafy/react-native-baidu-map-jm/master/images/android.jpg)

### Environments 环境要求
1.JS
- node: 8.0+

2.Android
- Android SDK: api 28+
- gradle: 4.5
- Android Studio: 3.1.3+

3.IOS
- XCode: 9.0+


### Install 安装
#### 使用 npm 源
npm install react-native-baidu-map-jm --save

### 原生模块导入

#### Android Studio
`react-native link react-native-baidu-map-jm`

PS: 工程build.gradle需要修改代码，否则无法运行：
```
dependencies {
    compileOnly 'com.facebook.react:react-native:+'
    compileOnly files('src/main/assets')
    implementation project(':map_baidu_lib')	//自定义导入百度SDK模块(第三方开发者使用时屏蔽此代码，打开下一行依赖)
//    implementation files('libs/BaiduLBS_Android.jar')     
}
```
修改为
```
dependencies {
    compileOnly 'com.facebook.react:react-native:+'
    compileOnly files('src/main/assets')
    implementation files('libs/BaiduLBS_Android.jar')     
}
```

#### IOS/Xcode
1、方式一：`react-native link react-native-baidu-map-jm`
2、方式二：
Podfile 增加
```
  pod 'React', :path => '../node_modules/react-native', :subspecs => [
    'Core',
    'CxxBridge',
    'DevSupport', 
    'RCTText',
    'RCTNetwork',
    'RCTWebSocket', 
    'RCTAnimation'
  ]
  pod 'yoga', :path => '../node_modules/react-native/ReactCommon/yoga'
  pod 'DoubleConversion', :podspec => '../node_modules/react-native/third-party-podspecs/DoubleConversion.podspec'
  pod 'glog', :podspec => '../node_modules/react-native/third-party-podspecs/glog.podspec'
  pod 'Folly', :podspec => '../node_modules/react-native/third-party-podspecs/Folly.podspec'

  pod 'react-native-baidu-map-jm', :podspec => '../node_modules/react-native-baidu-map-jm/ios/react-native-baidu-map-jm.podspec'
```

### Usage 使用方法

    import { MapView, MapTypes, Geolocation, Overlay} from 'react-native-baidu-map-jm'

#### MapView Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| zoomControlsVisible     | bool  | true     | 是否显示缩放控件，Android only
| trafficEnabled          | bool  | false    | 是否打开路况图层
| baiduHeatMapEnabled     | bool  | false    | 是否打开热力图
| mapType                 | number| 1        | 地图类型：标准地图=1、卫星地图=2
| zoom                    | number| 10       | 地图缩放级别
| center                  | object| null     | 地图中心位置，{latitude: 0, longitude: 0}
| buildingsEnabled        | bool  | true     | 是否显示3D楼块效果
| overlookEnabled         | bool  | true     | 是否打开俯仰角效果
| visualRange             | array | []       | 地图可是范围
| correctPerspective      | object| undefined| 是否支持透视效果，Android only
| onMapStatusChangeStart  | func  | undefined| 回调：地图状态开始更改，Android only
| onMapStatusChange       | func  | undefined| 回调：地图状态改变
| onMapStatusChangeFinish | func  | undefined| 回调：地图状态改变完成，Android only
| onMapLoaded             | func  | undefined| 回调：地图已加载完成
| onMapClick              | func  | undefined| 回调：地图点击
| onMapDoubleClick        | func  | undefined| 回调：双击地图
| onMarkerClick           | func  | undefined| 回调：点击Marker
| onMapPoiClick           | func  | undefined| 回调：点中底图标注
| onBubbleOfMarkerClick   | func  | undefined| 回调：点击气泡，Android only

#### Overlay 覆盖物
    const { Marker, Arc, Circle, Polyline, Polygon, InfoWindow } = Overlay;

##### Marker Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| tag                     | int   | -1       | 用于多个Marker时绑定InfoWindow，仅当tag相同时才会显示
| title                   | string| null     | 
| location                | object| {latitude: 0, longitude: 0}    |
| alpha                   | float | 1        | 透明度
| rotate                  | float | 0        | 图片旋转角度
| flat                    | bool  | null     | 
| icon                    | any   | null     | icon图片，同 <Image> 的 source 属性
| visible                 | bool  | true     | 是否显示

##### Arc Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| color                   | string| FFFF0088 |
| width                   | int   | 1        |
| poins                   | array | [{latitude: 0, longitude: 0}, {latitude: 0, longitude: 0}, {latitude: 0, longitude: 0}] | 数值长度必须为 3

##### Circle Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| radius                  | int   |          |
| fillColor               | string|          |
| stroke                  | object| {width: 2, color: 'AA0000FF'} |
| center                  | object| {latitude: 0, longitude: 0}       |

##### Polyline Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| points                  | array | [{latitude: 0, longitude: 0},{latitude: 0, longitude: 0}]     |
| width                   | int   | 8        |
| visible                 | bool  | false    |

##### Polygon Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| points                  | array | [{latitude: 0, longitude: 0}]     |
| fillColor               | string|          |
| stroke                  | object| {width: 2, color: 'AA00FF00'} |


##### Text Props 属性（iOS无此属性）
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| text                    | string|          |
| fontSize                | int   |          |
| fontColor               | string|          |
| bgColor                 | string|          |
| rotate                  | float |          |
| location                | object|{latitude: 0, longitude: 0}

##### InfoWindow Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| tag                     | int   | -1       | 用于多个Marker时绑定InfoWindow，仅当tag相同时才会显示
| location                | object| {latitude: 0, longitude: 0}，不要使用(无效)
| visible                 | bool  | false    | 是否隐藏
| title                   | string| ""       |

#### Geolocation Methods

| Method                    | Result
| ------------------------- | -------
| Promise reverseGeoCode(double lat, double lng) | `{"address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}`
| Promise reverseGeoCodeGPS(double lat, double lng) |  `{"address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}`
| Promise geocode(String city, String addr) | {"latitude": 0.0, "longitude": 0.0}
| Promise getCurrentPosition() | IOS: `{"latitude": 0.0, "longitude": 0.0, "address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}` Android: `{"latitude": 0.0, "longitude": 0.0, "direction": -1, "altitude": 0.0, "radius": 0.0, "address": "", "countryCode": "", "country": "", "province": "", "cityCode": "", "city": "", "district": "", "street": "", "streetNumber": "", "buildingId": "", "buildingName": ""}`

#### BaiduLocationModule Methods

| Method                    | Listener | Result | Description
| ------------------------- | -------  | ------ | -------
| Promise config(String key) | kLocationModuleCheckPermission |`{"errcode": "0", "errmsg": "Success"}`
| locationTimeout(int timeout) | null | null | 定位的超时时间(注意：不是定位频率)
| allowsBackground(bool allows) | null |null | 仅iOS有效
| startUpdatingLocation() | kLocationModuleUpdateLocation、kLocationModuleFail、kLocationModuleChangeAuthorization、kLocationModuleUpdateNetworkState | `{"method": "onLocationModuleUpdateLocation", "latitude": 0.0, "longitude": "0.0"}` or `{"method": "onLocationModuleFail", "errcode": 0, "errmsg": "定位发生错误"}` or `{"method": "onLocationModuleChangeAuthorization", "state": 0}` or `{"method": "onLocationModuleUpdateNetworkState", "state": 0}`| kLocationModuleChangeAuthorization、kLocationModuleUpdateNetworkState仅iOS有效
| stopUpdatingLocation() | null | null
| startUpdatingHeading() | kLocationModuleUpdateHeading |  `{"magneticHeading": 0.0, "trueHeading": 0.0, "headingAccuracy": 0.0, "timestamp": 0.0}`| 仅iOS有效
| stopUpdatingHeading() | null | null | 仅iOS有效


### Demo示例
```
import React, {Component} from 'react';
import {Platform, StyleSheet, Image, Text, View, Dimensions, Button,TouchableOpacity,NativeModules,NativeEventEmitter} from 'react-native';
import { MapView, MapTypes, Geolocation, Overlay} from 'react-native-baidu-map-jm';
const {height, width} = Dimensions.get('window');
const {
    BaiduLocationModule
} = NativeModules;

const locationListener = new NativeEventEmitter(BaiduLocationModule);

export default class App extends Component<Props> {
    state = {
        center:{
            latitude : 22.5801910000,
            longitude : 113.9276540000
        },
        trafficEnabled:true,
        isOpenPanoramic:false,
        mapType:1,
        markers:[
        {
             title:'汽车位置',
             location:{
                 latitude : 22.55373,
                 longitude : 113.925063
             }
        },
        {
             title:'haha，我在这里',
             location:{
                 latitude : 22.580054,
                 longitude : 113.927745
             }
        }],
        isCarLocation:false
    };

    componentWillMount() {
    }

    componentDidMount(){
        this.location(this.state.isCarLocation);
    }

    componentWillUnmount() {
    }

    //路况
    setTraffic(enabled){
        console.log('Home',"路况：" + enabled);
        this.setState({trafficEnabled:enabled});
    }

    //全景
    panoramic(isOpen){
        console.log('Home',"全景状态：" + isOpen);

    }

    //地图类型
    setMapType(type){
        console.log('Home',"地图类型：" + type);
        this.setState({mapType:type});
    }

    location(isCar){
        if(isCar) {
            console.log('定位车辆位置');
        } else {
            console.log('定位当前位置');
        }
        this.setState({isCarLocation:isCar});
        if(isCar){
            this.getCarLocation();
        }else{
            this.getMyLocation();
        }

        this.InfoWindowFunc.update();
    }

    //自定定位
    autoLocation() {
        locationListener.addListener(BaiduLocationModule.kLocationModuleCheckPermission, (reminder) => {
                                     console.log('kLocationModuleCheckPermission---->', reminder);
                                     BaiduLocationModule.startUpdatingLocation();
                                     });
        locationListener.addListener(BaiduLocationModule.kLocationModuleUpdateLocation, (reminder) => {
                                     console.log('kLocationModuleUpdateLocation---->', reminder);
                                     });
        BaiduLocationModule.config(null);
    }

    //查询车的位置
    getCarLocation(){
       //此处无Car的位置信息，用人的位置代替
      Geolocation.getCurrentPosition().then(data=>{
          console.log('getCarPosition',"latitude:" + data.latitude + " ,longitude:" + data.longitude + " ,address:" + data.address);
          let carPosition = this.state.markers[0];
          let personPosition = this.state.markers[1];
          this.setState({
              markers:[
                 {
                       title:carPosition.title,
                       location:{
                          latitude : data.latitude-0.001000,
                          longitude : data.longitude-0.001000,
                       }
                 },
                 {
                       title:personPosition.title,
                       location:{
                          latitude : personPosition.location.latitude,
                          longitude : personPosition.location.longitude,
                       }
                 }
              ],
              center:{
                        latitude : data.latitude-0.001000,
                        longitude : data.longitude-0.001000
              }
              });
          }).catch(e=>{
            console.log('getCurrentPosition', '获取位置失败:' + e);
        });
    }

    //获取当前人位置
    getMyLocation(){
        Geolocation.getCurrentPosition().then(data=>{
              console.log('getCurrentPosition',"latitude:" + data.latitude + " ,longitude:" + data.longitude + " ,address:" + data.address);
              let carPosition = this.state.markers[0];
              let personPosition = this.state.markers[1];
              this.setState({
                markers:[
                {
                     title:carPosition.title,
                     location:{
                         latitude : carPosition.location.latitude,
                         longitude : carPosition.location.longitude,
                     }
                 },
                 {
                     title:personPosition.title,
                     location:{
                         latitude : data.latitude,
                         longitude : data.longitude,
                     }
                 }
                 ],
                center:{
                    latitude : data.latitude,
                    longitude : data.longitude
                }
            });
        }).catch(e=>{
               console.log('getCurrentPosition', '获取位置失败:' + e);
        });
    }

  render() {
    return (
      <View style={styles.container}>
        <MapView
          mapType={this.state.mapType}
          trafficEnabled={this.state.trafficEnabled}
          width={width}
          height={height}
          zoom={18}
          center={this.state.center}

          onMapStatusChange={(params)=>{
//            console.log("onMapStatusChange->params:" + params.target.longitude)
          }}

          onMapLoaded={(params)=>{
            console.log("onMapLoaded->params:")
          }}

          onMapClick={(params)=>{
            console.log("onMapClick->位置:" + params.longitude + "," + params.latitude)
          }}

          onMapDoubleClick={(params)=>{
              console.log("onMapPoiClick->params:" + " ,位置:" + params.longitude + "," + params.latitude)
          }}

          onMapPoiClick={(params)=>{
            console.log("onMapPoiClick->Name:" + params.name + " ,uid:" + params.uid + " ,位置:" + params.longitude + "," + params.latitude)
          }}

          onMarkerClick={(params) => {
            console.log("onMarkerClick->标题:", params.title + " ,位置:" + params.position.longitude + "," + params.position.latitude)
          }}

          onBubbleOfMarkerClick={(params) => {
            console.log("onBubbleOfMarkerClick->标题:", params.title + " ,位置:" + params.position.longitude + "," + params.position.latitude)
            }}
        >
            
          <Overlay.Marker
            tag={0}
            title={this.state.markers[0].title}
            location={this.state.markers[0].location}
            icon={require('./res/image/icon_car.png')}
          />

          <Overlay.Marker
            tag={1}
            title={this.state.markers[1].title}
            location={this.state.markers[1].location}
            icon={require('./res/image/home_icon_locat.png')}
          />

            <Overlay.InfoWindow
            ref={(e)=>{this.InfoWindowFunc=e}}
            style={{flexDirection:'column', justifyContent:'space-between', width:100, height:100, backgroundColor:'#F5FCFF', borderRadius:6}}
            tag={this.state.isCarLocation? 0 : 1}
            visible={true}
            >

            <Text style={styles.baseText}>
            {"This is a Infowindow"}{'\n'}
            </Text>

            <Image
            source={require('./res/image/home_icon_car.png')}>
            </Image>
            </Overlay.InfoWindow>

          <Overlay.Circle
            center={this.state.markers[1].location}
            radius={30}
            fillColor='AA0000FF'
          />

          <Overlay.Polygon
            points={[{
                      latitude: this.state.markers[0].location.latitude-0.0005,
                      longitude: this.state.markers[0].location.longitude-0.0005
                     },
                     {
                      latitude: this.state.markers[0].location.latitude-0.0005,
                      longitude: this.state.markers[0].location.longitude+0.0005
                     },
                     {
                      latitude: this.state.markers[0].location.latitude+0.0005,
                      longitude: this.state.markers[0].location.longitude+0.0005
                     },
                     {
                      latitude: this.state.markers[0].location.latitude+0.0005,
                      longitude: this.state.markers[0].location.longitude-0.0005
                     }]}
          />

          <Overlay.Polyline
            points={[{
                      latitude: this.state.markers[0].location.latitude-0.0005,
                      longitude: this.state.markers[0].location.longitude-0.0005
                     },
                     {
                      latitude: this.state.markers[0].location.latitude+0.0005,
                      longitude: this.state.markers[0].location.longitude+0.0005
                     }]}
            />

            <Overlay.Arc
            points={[{
                      latitude: this.state.markers[0].location.latitude,
                      longitude: this.state.markers[0].location.longitude
                     },
                     {
                      latitude: this.state.markers[0].location.latitude,
                      longitude: this.state.markers[1].location.longitude
                     },
                     {
                      latitude: this.state.markers[1].location.latitude,
                      longitude: this.state.markers[1].location.longitude
                     }
                     ]}
          />

        </MapView>

        <TouchableOpacity style={{position:'absolute',bottom:20,start:20}} onPress={()=>{this.location(!this.state.isCarLocation)}}>
            <Image
            source={this.state.isCarLocation ? require('./res/image/home_icon_car.png'):require('./res/image/home_icon_location.png')}>
            </Image>
        </TouchableOpacity>

        <View style={{flexDirection:'column' , justifyContent:'space-between' ,width:30 ,height:90 ,position:'absolute',top:40,end:20}}>
            <TouchableOpacity onPress={()=>{this.setTraffic(!this.state.trafficEnabled)}}>
                <Image
                source={this.state.trafficEnabled ? require('./res/image/icon_traffic_selected.png'):require('./res/image/icon_traffic_unselected.png')}>
                </Image>
            </TouchableOpacity>
            <TouchableOpacity onPress={()=>{
                let type = this.state.mapType == 1 ? 2 : 1;
                this.setMapType(type);
                }}>
                <Image
                    source={this.state.mapType == 1 ? require('./res/image/icon_layer_unselected.png'):require('./res/image/icon_layer_selected.png')}>
                </Image>
            </TouchableOpacity>
        </View>

        <TouchableOpacity style={{position:'absolute',bottom:20,start:20}} onPress={()=>{this.location(!this.state.isCarLocation)}}>
            <Image
            source={this.state.isCarLocation ? require('./res/image/home_icon_location.png'):require('./res/image/home_icon_car.png')}>
            </Image>
        </TouchableOpacity>

      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  }
});

```
