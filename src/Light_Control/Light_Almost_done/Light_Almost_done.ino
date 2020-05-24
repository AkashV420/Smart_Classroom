#include <WiFi.h>
#include <HTTPClient.h>
#include<ArduinoJson.h>

#define RELAY_NO    true

// Set number of relays
#define NUM_RELAYS  19

// Assign each GPIO to a relay
int relayGPIOs[NUM_RELAYS] = {15,2,4,16,17,5,18,19,21,22,23,32,33,25,26,27,14,12,13};

const char* ssid = "arc-dell";
const char* password =  "54cqaVLt";
 
void setup() {
 
  Serial.begin(115200);
  delay(4000);
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi..");
  }
 
  Serial.println("Connected to the WiFi network");
  for(int i=1; i<=NUM_RELAYS; i++){
    pinMode(relayGPIOs[i-1], OUTPUT);
    if(RELAY_NO){
      digitalWrite(relayGPIOs[i-1], HIGH);
    }
    else{
      digitalWrite(relayGPIOs[i-1], LOW);
    }
  }
}
 
void loop() {
  String payload;
  if ((WiFi.status() == WL_CONNECTED)) { //Check the current connection status
 
    HTTPClient http;
 
    http.begin("http://10.42.0.1:4000/light/current_config"); //Specify the URL
    int httpCode = http.GET();                                        //Make the request
 
    if (httpCode > 0) { //Check for the returning code
 
        payload = http.getString();
        Serial.println(httpCode);
//        Serial.println(payload);
      }
 
    else {
      Serial.println(httpCode);
      Serial.println("Error on HTTP request");
    }
 
    http.end(); //Free the resources
  }
  
  Serial.println(payload);
  const size_t capacity = JSON_ARRAY_SIZE(1) + JSON_OBJECT_SIZE(5) + 150;
  DynamicJsonBuffer jsonBuffer(capacity);

  
  
  
  JsonArray& root = jsonBuffer.parseArray(payload);
  
  JsonObject& root_0 = root[0];
  const char * root_0_current_light_config = root_0["current_light_configuration"]; // 2
//  int root_0_id = root_0["id"]; // 10
//  const char* root_0_name = root_0["name"]; // "eaque et deleniti atque tenetur ut quo ut"
//  const char* root_0_email = root_0["email"]; // "Carmen_Keeling@caroline.name"
//  const char* root_0_body = root_0["body"]; // "voluptate iusto quis nobis reprehenderit ipsum amet nulla\nquia quas dolores velit et non\naut quia necessitatibus\nnostrum quaerat nulla et accusamus nisi facilis"
  Serial.println(root_0_current_light_config);
  Serial.println();
  int l=strlen(root_0_current_light_config);
  for(int i=0;i<l || i<NUM_RELAYS;i++){
    const char light_State=root_0_current_light_config[i];
    Serial.println(!(root_0_current_light_config[i]-'0'));
    if(RELAY_NO){
          Serial.print("NO ");
          digitalWrite(relayGPIOs[i], !(root_0_current_light_config[i]-'0'));
          Serial.println(!(root_0_current_light_config[i]-'0'));
        }
        else{
          Serial.print("NC ");
          digitalWrite(relayGPIOs[i], (root_0_current_light_config[i]-'0'));
          Serial.println((root_0_current_light_config[i]-'0'));
        }
  }
  delay(1000);
 
}
