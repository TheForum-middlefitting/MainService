import { TagCloud } from 'react-tagcloud'
import classes from "./Home.module.css"

const data = [
    { value: 'jQuery', count: 25 },
    { value: 'MongoDB', count: 18 },
    { value: 'JavaScript', count: 38 },
    { value: 'React', count: 30 },
    { value: 'Nodejs', count: 28 },
    { value: 'Express.js', count: 25 },
    { value: 'HTML5', count: 33 },
    { value: 'CSS3', count: 20 },
    { value: 'Webpack', count: 22 },
    { value: 'Babel.js', count: 7 },
    { value: 'ECMAScript', count: 25 },
    { value: 'Jest', count: 15 },
    { value: 'Mocha', count: 17 },
    { value: 'React Native', count: 27 },
    { value: 'Angular.js', count: 30 },
    { value: 'TypeScript', count: 15 },
    { value: 'Flow', count: 30 },
    { value: 'NPM', count: 11 },
]

const customRenderer = (tag, size, color) => (
    <span
        key={tag.value}
        className={classes.wordCloud}
        style={{
            animationDelay: `${Math.random() * 2}s`,
            fontSize: `${size * 1.5}em`,
            border: `2px solid ${color}`,
        }}
    >
    {tag.value}
  </span>
)
export default function Home() {
    return (
        <div className={classes.auth}>
            <TagCloud tags={data} minSize={1} maxSize={5} renderer={customRenderer} onClick={tag => alert(`'${tag.value}' was selected!`)}/>
        </div>
    )
}
