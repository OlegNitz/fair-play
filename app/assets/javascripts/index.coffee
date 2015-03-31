$ ->
  document.ws = new WebSocket $("body").data("ws-url")
  document.ws.onmessage = (event) ->
    message = JSON.parse event.data
    switch message.type
      when "Error"
        alert("Error: " + message.text)
      when "TradingVolume"
        tradingVolume = message.object
        updateTradingVolume(tradingVolume.currencyPair, tradingVolume.volume)
      else
        console.log(message)

getVolumesFromArray = (data) ->
  (v[1] for v in data)

getChartArray = (data) ->
  ([i, v] for v, i in data)

getChartOptions = (data) ->
  series:
    shadowSize: 0
  yaxis:
    min: getAxisMin(data)
    max: getAxisMax(data)
  xaxis:
    show: false

getAxisMin = (data) ->
  Math.min.apply(Math, data) * 0.9

getAxisMax = (data) ->
  Math.max.apply(Math, data) * 1.1

updateTradingVolume = (currencyPair, volume) ->
  if $("#" + currencyPair).size() == 0
    chart = $("<div>").addClass("chart").prop("id", currencyPair)
    chartHolder = $("<div>").addClass("chart-holder").append(chart)
    detailsHolder = $("<div>").addClass("details-holder")
    fancyCurrencyPair = currencyPair.replace("_", "/")
    flipper = $("<div>").addClass("flipper").append(chartHolder).append(detailsHolder).attr("data-content", fancyCurrencyPair)
    flipContainer = $("<div>").addClass("flip-container").append(flipper)
    $("#charts").prepend(flipContainer)
    data = [volume]
    plot = chart.plot([getChartArray(data)], getChartOptions(data)).data("plot")
  else
    plot = $("#" + currencyPair).data("plot")
    data = getVolumesFromArray(plot.getData()[0].data)
    if data.length > 100
      data.shift()
    data.push(volume)
    plot.setData([getChartArray(data)])
    # update the yaxes if either the min or max is now out of the acceptable range
    yaxes = plot.getOptions().yaxes[0]
    if ((getAxisMin(data) < yaxes.min) || (getAxisMax(data) > yaxes.max))
      # reseting yaxes
      yaxes.min = getAxisMin(data)
      yaxes.max = getAxisMax(data)
    # redraw the chart
    plot.setupGrid()
    plot.draw()
