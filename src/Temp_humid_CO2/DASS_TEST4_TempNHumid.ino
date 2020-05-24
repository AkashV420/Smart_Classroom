#include <Wire.h>
#include <SoftwareSerial.h>
#include "WiFi.h"
#include "HTTPClient.h"
#include "ThingSpeak.h"
// sda 21
// scl 22
char* wifi_ssid = "vishal-alienware-m15";
char* wifi_pwd = "1234567890";
WiFiClient  client;
unsigned long myChannelNumber = 1005926;
const char * myWriteAPIKey = "QEBUS2PBN6DUVSUU";
#define si7021Addr 0x40
String tempStr;

String tempMeasure()
{
  unsigned int data[2];
  Wire.beginTransmission(si7021Addr);
  //Send humidity measurement command
  Wire.write(0xF5);
  Wire.endTransmission();
  // Request 2 bytes of data
  Wire.requestFrom(si7021Addr, 2);
  // Read 2 bytes of data to get humidity
  if(Wire.available() == 2)
  {
    data[0] = Wire.read();
    data[1] = Wire.read();
  }
  // Convert the data
  float humidity  = ((data[0] * 256.0) + data[1]);
  humidity = ((125 * humidity) / 65536.0) - 6;
  Wire.beginTransmission(si7021Addr);
  // Send temperature measurement command
  Wire.write(0xF3);
  Wire.endTransmission();
  // Request 2 bytes of data
  Wire.requestFrom(si7021Addr, 2);
  // Read 2 bytes of data for temperature
  if(Wire.available() == 2)
  {
    data[0] = Wire.read();
    data[1] = Wire.read();
  }
  Serial.println("222");
  // Convert the data
  float temp  = ((data[0] * 256.0) + data[1]);
  float celsTemp = ((175.72 * temp) / 65536.0) - 46.85;
  float fahrTemp = celsTemp * 1.8 + 32;
  
  ThingSpeak.setField(1, String(celsTemp));
  ThingSpeak.setField(2, String(humidity));
   int x = ThingSpeak.writeFields(myChannelNumber, myWriteAPIKey);
   Serial.println("Posted:"+ String(x));
  
    if(x == 200){
      Serial.println("Channel update successful.");
    }
    else{
      Serial.println("Problem updating channel. HTTP error code " + String(x));
    }
  Serial.println("555");
  return (String(humidity) + " " + String(celsTemp));
}

void setup() { 
  Serial.begin(9600);
  Wire.begin();
  Wire.beginTransmission(si7021Addr);
  Wire.endTransmission();
  delay(1000);
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  WiFi.begin(wifi_ssid, wifi_pwd);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }


  Serial.println("Connected to the WiFi network");
  ThingSpeak.begin(client);  // Initialize ThingSpeak
  Serial.println("Setup done");
}

void loop()
{
   if(WiFi.status() != WL_CONNECTED) {
   WiFi.begin(wifi_ssid, wifi_pwd);
   while(WiFi.status() != WL_CONNECTED) {
      Serial.print('//');
    }
    Serial.println("Connected!");
  }
  Serial.println(tempMeasure());
  delay(30000);
}
