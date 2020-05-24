const express = require('express');
const lightroutes = express.Router();
let Light_layout = require('./schema/Light');
let Classroom = require('./schema/Classroom')
//NOTE  Registration route
lightroutes.route('/add').post(function(req, res) {
    console.log(req.body);
    let light_layout = new Light_layout(req.body);
    light_layout.save()
        .then(reg => {
            res.sendStatus(200);
        })
        .catch(err => {
            res.status(400).send("Failed to store to database");
        });
});

lightroutes.route('/add_class').post(function(req, res) {
    console.log(req.body);
    let classroom = new Classroom(req.body);
    classroom.save()
        .then(reg => {
            res.sendStatus(200);
        })
        .catch(err => {
            res.status(400).send("Failed to store to database");
        });
});


lightroutes.route('/activate').post(function(req, res) {
    Classroom.findOne({ classroom_name: req.body.classroom_name })
        .then(classroom => {
            console.log("updating light")
            if (!classroom) res.sendStatus(204);
            else {
                classroom.current_light_configuration=req.body.current_light_configuration;
                console.log(classroom);
                classroom.save()
                    .then(res.sendStatus(200))
                    .catch(res.sendStatus(204));
            }
        });
});

lightroutes.route('/current_config').get(function(req, res) {
    Classroom.find((err, data) => err ? res.status(400).send("Error occured") : res.json(data));
});



// Get allData
lightroutes.route('/all').get(function(req, res) {
    Light_layout.find((err, data) => err ? res.status(400).send("Error occured") : res.json(data));
});

module.exports = lightroutes;