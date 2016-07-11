/**
 * 
 */

var liquidChart = (function() {

var n = 243,
    duration = 750,
    now = new Date(Date.now() - duration),
    count = 0,
    countSucc = 0,
    countErr = 0,
    countRt = 0,
    scrollData = d3.range(n).map(function() { return 0; }),
	scrollDataSucc = d3.range(n).map(function() { return 0; });
	scrollDataErr = d3.range(n).map(function() { return 0; });

var margin = {top: 6, right: 0, bottom: 20, left: 40},
    width = 960 - margin.right,
    height = 360 - margin.top - margin.bottom;

var x = d3.time.scale()
    .domain([now - (n - 2) * duration, now - duration])
    .range([0, width]);

var y = d3.scale.linear()
    .range([height, 0]);

var line = d3.svg.line()
    .interpolate("basis")
    .x(function(d, i) { return x(now - (n - 1 - i) * duration); })
    .y(function(d, i) { return y(d); });

var svg = d3.select(".graph").append("p").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .style("margin-left", -margin.left + "px")
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

svg.append("defs").append("clipPath")
	.attr("id", "clip")
	.append("rect")
	.attr("width", width)
	.attr("height", height);

var axis = svg.append("g")
    .attr("class", "x axis")
    .attr("transform", "translate(0," + height + ")")
    .call(x.axis = d3.svg.axis().scale(x).orient("bottom"));

var yAxis = svg.append("g")
	.attr("transform", "translate(" + (margin.left) + ",0)")
    .call(y.axis = d3.svg.axis().scale(y).orient("left").ticks(5).tickSize(-width, 0, 0));

var path = svg.append("g")
    .attr("clip-path", "url(#clip)")
  .append("path")
    .datum(scrollData)
    .attr("class", "line");

var path1 = svg.append("g")
	.attr("clip-path", "url(#clip)")
	.append("path")
	.datum(scrollDataSucc)
	.attr("class", "succ-line");

var path2 = svg.append("g")
	.attr("clip-path", "url(#clip)")
	.append("path")
	.datum(scrollDataErr)
	.attr("class", "err-line");

var transition = d3.select({}).transition()
    .duration(750)
    .ease("linear");

d3.select(window)
    .on("scroll", function() { ++count; });

(function tick() {

	transition = transition.each(function() {

		transitionBody = function() {
			// update the domains
			now = new Date();
			x.domain([now - (n - 2) * duration, now - duration]);
			var yDomainMax = Math.max(d3.max(scrollData), d3.max(scrollDataSucc), d3.max(scrollDataErr));
			y.domain([0, yDomainMax]);

			// push the accumulated count onto the back, and reset the count
			scrollData.push(count);
			scrollDataSucc.push(countSucc);
			scrollDataErr.push(countErr);

			// redraw the line
			svg.select(".line")
			.attr("d", line)
			.attr("transform", null);

			// slide the x-axis left
			axis.call(x.axis);

			//adjust the y-axis
			yAxis.call(y.axis);

			// slide the line left
			path.transition()
			.attr("transform", "translate(" + x(now - (n - 1) * duration) + ")");

			svg.select(".succ-line")
			.attr("d", line)
			.attr("transform", null);
			path1.transition()
			.attr("transform", "translate(" + x(now - (n - 1) * duration) + ")");

			svg.select(".err-line")
			.attr("d", line)
			.attr("transform", null);
			path2.transition()
			.attr("transform", "translate(" + x(now - (n - 1) * duration) + ")");

			// pop the old data point off the front
			scrollData.shift();
			scrollDataSucc.shift();
			scrollDataErr.shift();
		}
		setTimeout(transitionBody, 100);
	}).transition().each("start", tick);
})();

	return {
		updateTps : function(val) {
			count = val;
		},
		updateSucc : function(val) {
			countSucc = val;
		},
		updateErr : function(val) {
			countErr = val;
		},
		updateRt : function(val) {
			countRt = val;
		}
	}
});
