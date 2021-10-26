import {
  requireNativeComponent,
  View,
  Platform,
  DeviceEventEmitter
} from 'react-native';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource';

export default class RNJMF1Monitor extends Component {
  static propTypes = {
    ...View.propTypes,
    image: PropTypes.number
  };

  _onChange(event) {
    if (typeof this.props[event.nativeEvent.type] === 'function') {
      this.props[event.nativeEvent.type](event.nativeEvent.params);
    }
  }

  render() {    
    return <JMF1Player {...this.props}
      style={Platform.OS === 'ios' ? {backgroundColor:'black',...this.props.style} : {...this.props.style, backgroundColor:'#FF000000'}}
      image={image} />;
  }
}

const JMF1Player = requireNativeComponent('JMF1PlayerView', RNJMF1Monitor);

