#!/bin/sh

SRCROOT=`pwd`
REACT_NATIVE_DIR=${SRCROOT}/node_modules/react-native
PATCH_FILE_PATH=${SRCROOT}/react-native-patch
echo "REACT_NATIVE_DIR:$REACT_NATIVE_DIR"
echo "PATCH_FILE_PATH:$PATCH_FILE_PATH"

set -x
patch  -p0 -N --dry-run --silent -f $REACT_NATIVE_DIR/React/Base/RCTModuleMethod.mm < $PATCH_FILE_PATH/rn_modify_module_method.patch 1>/dev/null
if [ $? -eq 0 ]; then
patch -p0 -f $REACT_NATIVE_DIR/React/Base/RCTModuleMethod.mm < $PATCH_FILE_PATH/rn_modify_module_method.patch
fi

set -x
patch  -p0 -N --dry-run --silent -f $REACT_NATIVE_DIR/React.podspec < $PATCH_FILE_PATH/rn_modify_react_podspec.patch 1>/dev/null
if [ $? -eq 0 ]; then
patch -p0 -f $REACT_NATIVE_DIR/React.podspec < $PATCH_FILE_PATH/rn_modify_react_podspec.patch
fi

set -x
patch  -p0 -N --dry-run --silent -f $REACT_NATIVE_DIR/React/Base/RCTBridgeModule.h < $PATCH_FILE_PATH/rn_modify_add_macro.patch 1>/dev/null
if [ $? -eq 0 ]; then
patch -p0 -f $REACT_NATIVE_DIR/React/Base/RCTBridgeModule.h < $PATCH_FILE_PATH/rn_modify_add_macro.patch
fi

