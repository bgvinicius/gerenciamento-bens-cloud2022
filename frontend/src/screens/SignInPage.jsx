import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import CheckCircleOutlinedIcon from '@mui/icons-material/CheckCircleOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@mui/material";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import {useContext, useState} from "react";
import {AuthContext, DataContext} from "../App";
import {api, setApiAuth} from "../api";
import { useNavigate } from "react-router-dom";

const theme = createTheme();

function SignUpDialog({isOpen, onClose}) {
    return <div>
        <Dialog open={isOpen} onClose={onClose}>
            <DialogTitle>{"Criação de conta"}</DialogTitle>
            <DialogContent>
                <DialogContentText sx={{mb: "12px"}}>
                    Preencha seus dados abaixo para criar uma nova conta
                </DialogContentText>

                <Box component="form" onSubmit={e => e.preventDefault()} noValidate sx={{mt: 1}}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="fullName"
                        label="Nome completo"
                        name="fullName"
                        autoComplete="fullName"
                        autoFocus
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="email"
                        label="E-mail"
                        name="email"
                        autoComplete="email"
                        autoFocus
                        type={"email"}
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="username"
                        label="Nome de usuario"
                        name="username"
                        autoComplete="username"
                        autoFocus
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Senha"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{mt: 3, mb: 2}}
                    >
                        Criar conta
                    </Button>
                </Box>

            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Fechar</Button>
            </DialogActions>
        </Dialog>
    </div>
}


export default function SignIn() {
    const [isSignupOpen, setSignupOpen] = useState(false)
    const { auth, setAuth} = useContext(AuthContext)
    const { data, setData } = useContext(DataContext)
    const navigate = useNavigate()

    function openSignupModal() {
        setSignupOpen(true)
    }

    function closeSignupModal() {
        setSignupOpen(false)
    }


    const handleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        const username = data.get('username')
        const password = data.get('password')

        // setApiAuth(username, password)
        setAuth({ username, password })
        api
            .get("/users/assets")
            .then(r => {
                setData({...data, goodsList: r.data})
                navigate("/goods")
            })
    };

    return (
        <ThemeProvider theme={theme}>
            <Container component="main" maxWidth={"lg"}>
                <Grid container spacing={2}>
                    <Grid item xs={7}>
                        <Paper sx={{
                            height: "100%"
                        }}>
                            <Box
                                sx={{
                                    marginTop: '8px',
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: 'center',
                                }}
                            >
                                <Avatar sx={{m: 1, bgcolor: 'secondary.main'}}>
                                    <CheckCircleOutlinedIcon/>
                                </Avatar>
                                <Typography component="h1" variant="h5" gutterBottom>
                                    Validar bem
                                </Typography>

                                <Typography variant="subtitle1">
                                    Insira abaixo o código do bem para validá-lo.
                                </Typography>

                                <Box component="form" onSubmit={() => {}} noValidate sx={{mt: 1}}>
                                    <TextField
                                        margin="normal"
                                        required
                                        fullWidth
                                        id="assetCode"
                                        label="Código do bem"
                                        name="assetCode"
                                        autoComplete="assetCode"
                                        autoFocus
                                    />

                                    <Button
                                        type="submit"
                                        fullWidth
                                        variant="contained"
                                        sx={{mt: 3, mb: 2}}
                                    >
                                        Ir para validação
                                    </Button>
                                </Box>
                            </Box>
                        </Paper>
                    </Grid>
                    <Grid item xs={3}>
                        <Paper sx={{
                            paddingLeft: '6px',
                            paddingRight: '6px',
                            height: "100%"
                        }}>

                            <Box
                                sx={{
                                    marginTop: '8px',
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: 'center',
                                }}
                            >
                                <Avatar sx={{m: 1, bgcolor: 'secondary.main'}}>
                                    <LockOutlinedIcon/>
                                </Avatar>
                                <Typography gutterBottom component="h1" variant="h5">
                                    Acessar Sistema
                                </Typography>

                                { !!auth ?
                                    <Box>
                                        <Typography sx={{textAlign: "center"}} variant="subtitle1">
                                            Você já está logado no sistema, clique no botão abaixo para entrar.
                                        </Typography>

                                        <Button
                                            type="submit"
                                            fullWidth
                                            variant="contained"
                                            sx={{mt: 3, mb: 2}}
                                        >
                                            Entrar
                                        </Button>
                                    </Box> :
                                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{mt: 1}}>
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            id="username"
                                            label="Nome de usuario"
                                            name="username"
                                            autoComplete="username"
                                            autoFocus
                                        />
                                        <TextField
                                            margin="normal"
                                            required
                                            fullWidth
                                            name="password"
                                            label="Senha"
                                            type="password"
                                            id="password"
                                            autoComplete="current-password"
                                        />
                                        <Button
                                            type="submit"
                                            fullWidth
                                            variant="contained"
                                            sx={{mt: 3, mb: 2}}
                                        >
                                            Login
                                        </Button>
                                        <Grid container direction={"row-reverse"}>
                                            <Grid item>
                                                <Button onClick={openSignupModal} size={"small"}
                                                        sx={{textDecoration: "underline"}}>
                                                    {"Crie sua conta"}
                                                </Button>
                                            </Grid>
                                        </Grid>
                                    </Box>
                                }
                            </Box>
                        </Paper>
                        <CssBaseline/>
                    </Grid>
                </Grid>
            </Container>
            <SignUpDialog isOpen={isSignupOpen} onClose={closeSignupModal}></SignUpDialog>
        </ThemeProvider>
    );
}