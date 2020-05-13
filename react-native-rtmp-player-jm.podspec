Pod::Spec.new do |s|
  s.name         = "react-native-rtmp-player-jm"
  s.version      = "1.2.0"
  s.summary      = "Jimi RTMP Video Player SDK for React Native"

  s.description  = <<-DESC
  Jimi RTMP Video Player SDK for React Native
                   DESC

  s.homepage     = "https://github.com/Eafy/react-native-rtmp-player-jm"

  s.license      = "MIT"
  s.author       = { "Eafy" => "lizhijian_21@163.com" }
  s.platform     = :ios, "8.0"
  s.requires_arc = true

  s.source       = { :git => "https://github.com/Eafy/react-native-rtmp-player-jm.git", :tag => "#{s.version}" }

  s.source_files  = "ios/**/*.{h,m}"
  #s.ios.vendored_frameworks = "ios/**/*.{framework}"
  s.libraries = "c++"

  s.dependency 'React'
  s.dependency 'JimiVideoPlayer'
end