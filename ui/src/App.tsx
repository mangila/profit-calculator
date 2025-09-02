import {useEffect, useState} from 'react'
import {LineChart} from "@mui/x-charts";
import {Box, Button, CircularProgress, Grid, Typography} from "@mui/material";

interface Pair {
    day: { value: number };
    price: { value: number };
}

interface Dataset {
    data: Pair[];
}

interface Profit {
    buyDay: number;
    sellDay: number;
}

function App() {
    const [chartData, setChartData] = useState<{ xData: number[], yData: number[] } | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [selectedLevel, setSelectedLevel] = useState(1);
    const [buyDay, setBuyDay] = useState(0);
    const [sellDay, setSellDay] = useState(0);

    const fetchDataset = async (level: number) => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch(`http://localhost:8080/api/v1/data/${level}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data: Dataset = await response.json();

            const xData = data.data.map(pair => pair.day.value);
            const yData = data.data.map(pair => pair.price.value);

            setChartData({xData, yData});
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setLoading(false);
        }
    };

    const fetchProfit = async (level: number) => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch(`http://localhost:8080/api/v1/data/profit/${level}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data: Profit = await response.json();

            setBuyDay(data.buyDay);
            setSellDay(data.sellDay);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDataset(selectedLevel);
        fetchProfit(selectedLevel);
    }, [selectedLevel]);

    const handleLevelChange = (level: number) => {
        setSelectedLevel(level);
    };

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <CircularProgress/>
            </Box>
        );
    }

    if (error) {
        return (
            <Box display="flex" flexDirection="column" justifyContent="center" alignItems="center" height="100vh">
                <Typography color="error" variant="h6" gutterBottom>
                    Error: {error}
                </Typography>
                <Button variant="contained" onClick={() => fetchDataset(selectedLevel)}>
                    Retry
                </Button>
            </Box>
        );
    }

    return (
        <>
            <Grid container direction="column" spacing={2}>
                <Grid size={12}>
                    <Box display="flex" justifyContent="center" gap={2} mb={2}>
                        {[1, 2, 3, 4].map(level => (
                            <Button
                                key={level}
                                variant={selectedLevel === level ? "contained" : "outlined"}
                                onClick={() => handleLevelChange(level)}
                            >
                                Dataset {level}
                            </Button>
                        ))}
                    </Box>
                </Grid>

                <Grid size={12}>
                    <Box display="flex" justifyContent="center" alignItems="center">
                        {chartData && (
                            <LineChart
                                xAxis={[{data: chartData.xData}]}
                                series={[
                                    {
                                        data: chartData.yData,
                                        label: `Dataset ${selectedLevel}`
                                    },
                                ]}
                                loading={loading}
                                height={500}
                                width={1200}
                                grid={{vertical: true, horizontal: true}}
                            />
                        )}
                    </Box>
                </Grid>
                <Grid display="flex" justifyContent="center" alignItems="center" spacing={10} container>
                    <Grid>
                        Buy: {buyDay}
                    </Grid>
                    <Grid>
                        Sell: {sellDay}
                    </Grid>
                </Grid>
            </Grid>
        </>
    )
}

export default App