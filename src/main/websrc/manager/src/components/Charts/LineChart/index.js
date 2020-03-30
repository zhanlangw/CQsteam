import React from 'react';
import { Chart, Tooltip, Geom, Legend, Axis } from 'bizcharts';
import DataSet from '@antv/data-set';
import Slider from 'bizcharts-plugin-slider';
import autoHeight from '../autoHeight';
import styles from './index.less';
import moment from 'moment';


@autoHeight()
export default class LineChart extends React.Component {
  render() {
    let {
      showSider,
      title,
      height = 400,
      padding = [60, 20, 40, 40],
      titleMap = {
        y1: 'y1',
        y2: 'y2',

      },
      borderWidth = 2,
      data = [
        {
          x: 0,
          y1: 0,
          y2: 0,
        },
      ],
    } = this.props;

    if(data.length === 0){
      data = [
        {
          x: 0,
          y1: 0,
          y2: 0,
        },
      ]
    };

    const ds = new DataSet({
      state: {
        start: data[0].x,
        end: data[data.length - 1].x,
      },
    });

    const dv = ds.createView();
    dv.source(data)
      .transform({
        type: 'filter',
        callback: obj => {
          const date = obj.x;
          //moment(date).format('x');
          return moment(date).format('x') <= moment(ds.state.end).format('x') && date >= moment(ds.state.start).format('x');
        },
      })

      .transform({
        type: 'map',
        callback(row) {
          const newRow = { ...row };
          newRow[titleMap.y1] = row.y1;
          newRow[titleMap.y2] = row.y2;
          return newRow;
        },
      })
      .transform({
        type: 'fold',
        fields: [titleMap.y1, titleMap.y2], // 展开字段集
        key: 'key', // key字段
        value: 'value', // value字段
      });

    const cols = {
      x: {
      range: [ 0, 1 ]
      }

    }

    const SliderGen = () => (
      <Slider
        padding={[0, padding[1] + 20, 0, padding[3]]}
        width="auto"
        height={26}
        xAxis="x"
        yAxis="y1"
        scales={{ x: timeScale }}
        data={data}
        start={ds.state.start}
        end={ds.state.end}
        backgroundChart={{ type: 'line' }}
        onChange={({ startValue, endValue }) => {
          ds.setState('start', startValue);
          ds.setState('end', endValue);
        }}
      />
    );

    return (
      <div className={styles.timelineChart} style={{ height: height + 30 }}>
        <div>
          {title && <h4>{title}</h4>}
          <Chart height={height} padding={padding} data={dv} scale={cols} forceFit>
            <Axis name="x" />
            <Tooltip />
            {/* <Legend useHtml={true} position="top" marker="circle"/> */}
            <Geom 
              type="line" 
              position="x*value" 
              // shape={'smooth'} 
              size={borderWidth} 
              color="key" 
            />
          </Chart>
          {
            showSider &&
            <div style={{ marginRight: -20 }}>
              <SliderGen />
            </div>
          }

        </div>
      </div>
    );
  }
}
