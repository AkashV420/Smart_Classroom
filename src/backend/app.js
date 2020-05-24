// BASE SETUP
// =============================================================================
// call the packages we need
var express    = require('express');        // call express
var bodyParser = require('body-parser');
var pgp = require('pg-promise')();
var request = require('request');
var sleep = require('sleep');
var fs = require('fs'); 

const {Builder, By, until} = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');


var app        = express();                 // define our app using express
var db = pgp('postgres://bpvonkohqidbzl:dd067033f8852d340d850fac10045340fe540372dcfdb382acfbb95492b145c5@ec2-46-137-84-173.eu-west-1.compute.amazonaws.com:5432/dfl3f5v8998tn3')


db.none('CREATE TABLE IF NOT EXISTS lights1 (id serial primary key, current_config varchar(255))')
  .then(() => {
	  	db.none('INSERT INTO lights1 (current_config) values ($1)',['1100110011001100110']);
  		console.log("successfully connected to table.");
  })
  .catch(error => {
  		console.log(error);
  })


db.none("CREATE TABLE IF NOT EXISTS ac9 (id serial primary key, acNo int unique, power bool default false, temp int default 22, mode varchar(255) default 'COOL', swing bool default false)")
  .then(() => {
	  	db.none('INSERT INTO ac9 (acNo) values (1)');
	  	db.none('INSERT INTO ac9 (acNo) values (2)');
	  	db.none('INSERT INTO ac9 (acNo) values (3)');
	  	db.none('INSERT INTO ac9 (acNo) values (4)');
  		console.log("successfully connected to ac table.");
  })
  .catch(error => {
  		console.log(error);
  })  
db.none("CREATE TABLE IF NOT EXISTS projector (id serial primary key, projectorNo int unique, power bool default false, input varchar(255), aspectRatio varchar(255))")
  .then(() => {
	  	db.none('INSERT INTO projector (projectorNo) values (1)');
	  	db.none('INSERT INTO projector (projectorNo) values (2)');
  		console.log("successfully connected to projector table.");
  })
  .catch(error => {
  		console.log(error);
  })

db.none("CREATE TABLE IF NOT EXISTS events2 (id serial primary key, slotNo int unique, name varchar(255), projector bool, boardLight bool, audienceLights bool, date date, timeSlot int, occupancy int)")
  .then(() => {
	  	db.none("INSERT INTO events2 values (1,1,$1,false,true,false,$2,1,100)",['event1','2020-04-23']);
	  	db.none("INSERT INTO events2 values (2,2,$1,true,false,false,$2,2,10)",['event2','2020-04-23']);
  		console.log("successfully connected to events table.");
  })
  .catch(error => {
  		console.log(error);
  })    

db.none("CREATE TABLE IF NOT EXISTS classroom (id serial primary key, inTemp float, outTemp float, inHumidity int, outHumidity int, co2 int, timeSlot int, power float)")
  .then(() => {
	  	db.none('INSERT INTO classroom values (1,20.5,25.3,2,3,1,1,7.5)');
  		console.log("successfully connected to classroom table.");
  })
  .catch(error => {
  		console.log(error);
  })    


// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


//INDEX ROUTE
app.get("/", function (req, res) {
    return res.send({ message: "Hello world" })
});



app.get("/classroom/currentStats", function (req, res) {

	db.any('SELECT * FROM classroom ORDER BY id DESC LIMIT 1;')
    .then(function(data) {
    	console.log(data);
        return res.status(200).send(data[0]);
    })
    .catch(function(error) {
        console.log(error);
        return res.status(204).send({ error: error});
        // error;
    });
});


app.post("/classroom/updateStats", function (req, res) {
	const {inTemp, outTemp, inHumidity, outHumidity, co2, power} = req.body;
	db.one('INSERT INTO classroom (inTemp, outTemp, inHumidity, outHumidity, co2, power) values($1,$2,$3,$4,$5,$6)',[inTemp, outTemp, inHumidity, outHumidity, co2, power])
    .then(() => {
        return res.status(200);
    })
    .catch(function(error) {
        console.log(error);
        return res.status(204).send({ error: error});
    });
});


app.post("/lights/activate", function (req, res) {
	const {current_config} = req.body;
	db.one('INSERT INTO lights1 (current_config) values ($1)',[current_config])
    .then(() => {
        return res.status(200);
    })
    .catch(function(error) {
        console.log(error);
        return res.status(204).send({ error: error});
    });
});


app.get("/lights/current_config", function (req, res) {

	db.any('SELECT * FROM lights1 ORDER BY id DESC LIMIT 1')
    .then(function(data) {
    	console.log(data);
        return res.status(200).send(data[0]);
    })
    .catch(function(error) {
        console.log(error);
        return res.status(204).send({ error: error});
        // error;
    });
});


app.get("/ac:acno/current_stats", function (req, res) {

	var acno = req.params.acno;
	console.log(acno);
	db.any('SELECT * FROM ac9 WHERE acNo=$1',[acno])
    .then(function(data) {
    	console.log(data);
        return res.status(200).send(data[0]);
    })
    .catch(function(error) {
        console.log(error);
        return res.status(204).send({ error: error});
        // error;
    });
});


app.get("/event/get_current/:slotNo", function (req, res) {

	var slotNo = req.params.slotNo;
	db.any('SELECT * FROM events2 WHERE slotNo=$1',[slotNo])
    .then(function(data) {
    	console.log(data);
        return res.status(200).send(data[0]);
    })
    .catch(function(error) {
        console.log(error);
        return res.status(204).send({ error: error});
        // error;
    });
});

app.get("/projector:projno/current_stats", function (req, res) {

	var projno = req.params.projno;
	// console.log(projno);
	db.any('SELECT * FROM projector WHERE projectorNo=$1',[projno])
    .then(function(data) {
    	console.log(data);
        return res.status(200).send(data[0]);
    })
    .catch(function(error) {
        console.log(error);
        return res.status(204).send({ error: error});
        // error;
    });
});




app.get("/ac:acno/:fxn/:val", function (req, res) {
	
	var acno = req.params.acno;

	var fxn = req.params.fxn;
	var val = req.params.val;

	var no1 = null;
	var no2 = null;
	
	if (fxn == 'set_temp'){
		no1 = 1;
		db.none('UPDATE ac9 SET temp=$1 WHERE acNo=$2', [val,acno])
		no2 = val;
	}
	else if (fxn == 'set_mode'){
		no1 = 2;
		db.none('UPDATE ac9 SET mode=$1 WHERE acNo=$2', [val,acno])
		if (val == 'DRY'){
			no2 = 1;
		}
		else if (val == 'COOL'){
			no2 = 2;
		}
		else if (val == 'FAN'){
			no2 = 3;
		}
	}
	else if (fxn == 'set_swing'){
		no1 = 3;
		if (val == 'ON'){
			db.none('UPDATE ac9 SET swing=true WHERE acNo=$1', [acno])
			no2 = 1;
		} 
		else {
			db.none('UPDATE ac9 SET swing=false WHERE acNo=$1', [acno])
			no2 = 0;
		}
	}
	else if (fxn == 'power'){
		no1 = 4;
		if (val == 'ON'){
			db.none('UPDATE ac9 SET power=true WHERE acNo=$1', [acno])
			no2 = 1;
		}
		else {
			db.none('UPDATE ac9 SET power=false WHERE acNo=$1', [acno])
			no2 = 0;
		}
	}
	else{
        return res.status(204).send({ error: "Wrong Request!"});
	}


	var newlogin = 0;
	var theaccesstoken = undefined;
	if(!fs.existsSync('access.json')) {
		newlogin = 1;
		console.log("no access file, new login!");
	}
	else {
		data = fs.readFileSync('access.json'); 
		let thedata = JSON.parse(data)					
		timenow = new Date();
		if (timenow - Date.parse(thedata.time) > 170000000){
			newlogin = 1;
			console.log("Time Up, new login!");
		}	
		else{
			newlogin = 0;
			theaccesstoken = thedata.access_token;
		}

    }
    

	if (newlogin == 1){
		let o = new chrome.Options();
		// o.addArguments('start-fullscreen');
		// o.addArguments('disable-infobars');
		o.addArguments('headless');
		o.addArguments("--disable-gpu");
		o.addArguments("--no-sandbox");
		o.setUserPreferences({ credential_enable_service: false });

	    driver = new Builder()
	        .setChromeOptions(o)
	        .forBrowser('chrome')
	        .build();


	    loginUrl = "https://eazyapi.b1hub.com/OpenApi/authorize?client_id=76dca191-4ade-498b-8655-9fcfd8efc295&client_secret=0d4215e6-3da7-4728-9a25-4cd305f89e79&redirection_url=http://cbs.iiit.ac.in&response_type=code"    

	 	driver.get(loginUrl)
	 	.then(() => {
	 		driver.findElement(By.id("email_id")).sendKeys("av9622130984@gmail.com")
	 		.then(() =>{
			 	driver.findElement(By.id("password")).sendKeys("h105@iiit")
			 	.then(() =>{
				 	driver.findElement(By.className("login_btn")).click()
				 	.then(async function() {
					    // await driver.wait(until.elementLocated(By.className("login_btn")), 15000, 'Looking for element');
					 	btns = await driver.findElements(By.className("login_btn"))
				 		btns[1].click()	
						.then(async function(){
						 	theurl = await driver.getCurrentUrl()
						    driver.quit()
						    // console.log(theurl)
						 	thecode = theurl.split("code=")[1].split("&client")[0]
						    // console.log(thecode)
						 	request.post("https://eazyapi.b1hub.com/Openapi/token", { 
						 		json: {
								    "grant_type": "code",
								    "code": thecode,
								    "hub_id": "0CB2B706BFF5",
								    "client_id": "76dca191-4ade-498b-8655-9fcfd8efc295",
								    "client_secret": "0d4215e6-3da7-4728-9a25-4cd305f89e79",
								    "redirect_uri": "http://cbs.iiit.ac.in"
						 		} 
						 	}, (error, res2, body) => {
								if (error) { 
								  	console.log(error); 
							        return res.status(400).send({ error: "Error!"});
								}
								else {
									thetimenow = new Date();
									let accessData = {
									    access_token: body.access_token,
									    time: thetimenow
									};
										 
									let data = JSON.stringify(accessData, null, 2);

									fs.writeFile('access.json', data, (error) => {
									    if (error){
									    	console.log(error); 
									        return res.status(400).send({ error: "Error!"})
									    }
									    console.log('Data written to file');
									});


								  
									  	
								  request.post("https://eazyapi.b1hub.com/thirdparty/v1/control/exec", { 
								 		json: {
										    "access_token": "Bearer"+body.access_token,
										    "hub_id": "0CB2B706BFF5",
										    "details": 
											{
										    "op": "control",
										    "device_id": "7da8d1f1-3d28-47f9-a09e-223ed7c7a93b",
										    "device_b_one_id": "0005",
										    "commands": 
										    			{
										    				"key": no1,
														    "key_value": no2
										    			}
										    }
								 		} 
								 	}, (error, res2, body) => {
									  if (error) { 
									  	console.log(error); 
								        return res.status(400).send({ error: "Error!"});
									  }	
									  return res.send({ message: "Turned the AC on!" });
									});  
								}  
							});
						});	 		
			 		});
			 	});
		 	});
	 	});
	 }
	 else{
	 	request.post("https://eazyapi.b1hub.com/thirdparty/v1/control/exec", { 
	 		json: {
			    "access_token": "Bearer"+theaccesstoken,
			    "hub_id": "0CB2B706BFF5",
			    "details": 
				{
			    "op": "control",
			    "device_id": "7da8d1f1-3d28-47f9-a09e-223ed7c7a93b",
			    "device_b_one_id": "0005",
			    "commands": 
			    			{
			    				"key": no1,
							    "key_value": no2
			    			}
			    }
	 		} 
	 	}, (error, res2, body) => {
		  if (error) { 
		  	console.log(error); 
	        return res.status(400).send({ error: "Error!"});
		  }	
		  return res.send({ message: "Turned the AC on!" });
		});

	 }	
    // driver.findElement(By.name(name));





})



app.get("/projector:projectorno/:fxn/:val", function (req, res) {
	
	var projectorno = req.params.projectorno;
	var fxn = req.params.fxn;
	var val = req.params.val;

	var no3 = null;
	var no4 = null;
	
	if (fxn == 'input'){
		db.none('UPDATE projector SET input=$1 WHERE projectorNo=$2', [val,projectorno])
        return res.status(200).send({ success: true});
	}
	else if (fxn == 'aspectRatio'){
		db.none('UPDATE projector SET aspectRatio=$1 WHERE projectorNo=$2', [val,projectorno])
        return res.status(200).send({ success: true});
	}
	else if (fxn == 'power'){
		no3 = 1;
		if (val == 'ON'){
			db.none('UPDATE projector SET power=true WHERE projectorNo=$1', [projectorno])
			no4 = 1;
		}
		else {
			db.none('UPDATE projector SET power=false WHERE projectorNo=$1', [projectorno])
			no4 = 0;
		}
	}
	else{
        return res.status(204).send({ error: "Wrong Request!"});
	}



	var newlogin = 0;
	var theaccesstoken = undefined;
	if(!fs.existsSync('access.json')) {
		newlogin = 1;
		console.log("no access file, new login!");
	}
	else {
		data = fs.readFileSync('access.json'); 
		let thedata = JSON.parse(data)					
		timenow = new Date();
		if (timenow - Date.parse(thedata.time) > 170000000){
			newlogin = 1;
			console.log("Time Up, new login!");
		}	
		else{
			newlogin = 0;
			theaccesstoken = thedata.access_token;
		}

    }
    

	if (newlogin == 1){
		let o = new chrome.Options();
		// o.addArguments('start-fullscreen');
		// o.addArguments('disable-infobars');
		o.addArguments('headless');
		o.addArguments("--disable-gpu");
		o.addArguments("--no-sandbox");
		o.setUserPreferences({ credential_enable_service: false });

	    driver = new Builder()
	        .setChromeOptions(o)
	        .forBrowser('chrome')
	        .build();


	    loginUrl = "https://eazyapi.b1hub.com/OpenApi/authorize?client_id=76dca191-4ade-498b-8655-9fcfd8efc295&client_secret=0d4215e6-3da7-4728-9a25-4cd305f89e79&redirection_url=http://cbs.iiit.ac.in&response_type=code"    

	 	driver.get(loginUrl)
	 	.then(() => {
	 		driver.findElement(By.id("email_id")).sendKeys("av9622130984@gmail.com")
	 		.then(() =>{
			 	driver.findElement(By.id("password")).sendKeys("h105@iiit")
			 	.then(() =>{
				 	driver.findElement(By.className("login_btn")).click()
				 	.then(async function() {
					    // await driver.wait(until.elementLocated(By.className("login_btn")), 15000, 'Looking for element');
					 	btns = await driver.findElements(By.className("login_btn"))
				 		btns[1].click()	
						.then(async function(){
						 	theurl = await driver.getCurrentUrl()
						    driver.quit()
						    // console.log(theurl)
						 	thecode = theurl.split("code=")[1].split("&client")[0]
						    // console.log(thecode)
						 	request.post("https://eazyapi.b1hub.com/Openapi/token", { 
						 		json: {
								    "grant_type": "code",
								    "code": thecode,
								    "hub_id": "0CB2B706BFF5",
								    "client_id": "76dca191-4ade-498b-8655-9fcfd8efc295",
								    "client_secret": "0d4215e6-3da7-4728-9a25-4cd305f89e79",
								    "redirect_uri": "http://cbs.iiit.ac.in"
						 		} 
						 	}, (error, res2, body) => {
								if (error) { 
								  	console.log(error); 
							        return res.status(400).send({ error: "Error!"});
								}
								else {
									thetimenow = new Date();
									let accessData = {
									    access_token: body.access_token,
									    time: thetimenow
									};
										 
									let data = JSON.stringify(accessData, null, 2);

									fs.writeFile('access.json', data, (error) => {
									    if (error){
									    	console.log(error); 
									        return res.status(400).send({ error: "Error!"})
									    }
									    console.log('Data written to file');
									});


								  
									  	
								  request.post("https://eazyapi.b1hub.com/thirdparty/v1/control/exec", { 
								 		json: {
										    "access_token": "Bearer"+body.access_token,
										    "hub_id": "0CB2B706BFF5",
										    "details": 
											{
										    "op": "control",
										    "device_id": "1e2c364a-2d54-41e3-9764-e6748034b84c",
										    "device_b_one_id": "0006",
										    "commands": 
										    			{
										    				"key": no3,
														    "key_value": no4
										    			}
										    }
								 		} 
								 	}, (error, res2, body) => {
									  if (error) { 
									  	console.log(error); 
								        return res.status(400).send({ error: "Error!"});
									  }	
									  return res.send({ message: "Turned the projector off!" });
									});  
								}  
							});
						});	 		
			 		});
			 	});
		 	});
	 	});
	 }
	 else{
	 	request.post("https://eazyapi.b1hub.com/thirdparty/v1/control/exec", { 
	 		json: {
			    "access_token": "Bearer"+theaccesstoken,
			    "hub_id": "0CB2B706BFF5",
			    "details": 
				{
			    "op": "control",
			    "device_id": "1e2c364a-2d54-41e3-9764-e6748034b84c",
			    "device_b_one_id": "0006",
			    "commands": 
			    			{
			    				"key": no3,
							    "key_value": no4
			    			}
			    }
	 		} 
	 	}, (error, res2, body) => {
		  if (error) { 
		  	console.log(error); 
	        return res.status(400).send({ error: "Error!"});
		  }	
		  return res.send({ message: "Turned the projector off!" });
		});

	 }	
    // driver.findElement(By.name(name));

})




app.listen(process.env.PORT || 5000, function () {
    console.log("App is running");
});

module.exports = app;

