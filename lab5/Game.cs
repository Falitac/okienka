using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class Game : MonoBehaviour
{
    public class RockPosition {
        public GameObject rockRef;
        public Vector3 destinationPosition;
    }
    public GameObject resultTextA;
    public GameObject resultTextB;
    public GameObject turnInfoText;

    ArrayList buttons;

    public GameObject rock;
    public int initRockCount = 4;
    ArrayList rocks;
    ArrayList rocksPositions;
    ArrayList rocksWorldPositions;

    Color [] playerColors = new Color[2];
    Color playerA = new Color(0.7f, 0.3f, 0.2f); 
    Color playerB = new Color(0.2f, 0.3f, 0.7f);

    int playerAPoints = 0;
    int playerBPoints = 0;
    char playerTurn = 'A';

    private int indexer = 0;

    enum MoveResult
    {
        ChangeTurn,
        KeepTurn,
        None
    }

    [SerializeField]
    private float ANIM_TOLERANCE = 0.02f;
    [SerializeField]
    private float ANIM_SPEED = 5.0f;

    void Start()
    {
        rocks = new ArrayList(12 * initRockCount);
        rocksPositions = new ArrayList(12 * initRockCount);
        rocksWorldPositions = new ArrayList(12 * initRockCount);
        buttons = new ArrayList(12);

        for(int i = 0; i < 12; i++)
        {
            for(int j = 0; j < initRockCount; j++)
            {
                char player = i < 6 ? 'A' : 'B';
                SpawnRocks(player, i);
                rocksPositions.Add(i);
            }
            AlignRocks(i);

            var buttonHolder = GameObject.Find("HoleButton" + i);
            if(buttonHolder == null)
            {
                continue;
            }
            var button = buttonHolder.GetComponent<Button>();
            int buttonID = i;
            button.onClick.AddListener(delegate { HoleClicked(buttonID); });
            buttons.Add(button);

        }
        UpdateRockCounters();

        playerTurn = 'A';
        turnInfoText.GetComponent<TMP_Text>().text = $"Turn {playerTurn}";
        turnInfoText.GetComponent<TMP_Text>().color = playerA;

        resultTextA.GetComponent<UnityEngine.UI.Text>().color = playerA;
        resultTextB.GetComponent<UnityEngine.UI.Text>().color = playerB;
    }

    private void HoleClicked(int id)
    {
        Debug.Log("Clicked" + id);
        if(playerTurn == 'A' && (id < 0 || id >= 6) ||
           playerTurn == 'B' && (id < 6 || id >= 12)
        )
        {
            Debug.Log("Invalid move" + id);
            return;
        }
        var status = ProgressRocks(id);
        if(status != MoveResult.ChangeTurn)
        {
            return;
        }
        if(playerTurn == 'A')
        {
            playerTurn = 'B';
            turnInfoText.GetComponent<TMP_Text>().text = $"Turn {playerTurn}";
            turnInfoText.GetComponent<TMP_Text>().color = playerB;

        } else
        {
            playerTurn = 'A';
            turnInfoText.GetComponent<TMP_Text>().text = $"Turn {playerTurn}";
            turnInfoText.GetComponent<TMP_Text>().color = playerA;
            turnInfoText.GetComponent<TMP_Text>().material.color = playerA;
        }
        Debug.Log("Is A empty?" + IsPlayerSideEmpty('A'));
        Debug.Log("Is B empty?" + IsPlayerSideEmpty('B'));


        /*
        if(IsPlayerSideEmpty('A'))
        {
            for(int i = 0; i < rocksPositions.Count; i++)
            {
                (int)rocksPositions[i] ==;
            }
        }
        if(playerAPoints > playerBPoints)
        {
            turnInfoText.GetComponent<TMP_Text>().text = "Player A won!";
        } else if(playerAPoints == playerBPoints)
        {
            turnInfoText.GetComponent<TMP_Text>().text = "Draw!";
        } else
        {
            turnInfoText.GetComponent<TMP_Text>().text = "Player B won!";
        }
        */
    }

    bool IsPlayerSideEmpty(char player)
    {
        for(int i = 0; i < rocksPositions.Count; i++)
        {
            int pos = (int)rocksPositions[i];
            if(player == 'A' && 0 <= pos && pos < 6)
            {
                return false;

            } else if (player == 'B' && 6 <= pos && pos < 12)
            {
                return false;
            }
        }
        return true;
    }

    void SpawnRocks(char playerIdent, int holeNumber)
    {
        GameObject hole = GameObject.Find("/Player" + playerIdent + "/Hole" + holeNumber);
        var newRock = Instantiate(rock);
        newRock.transform.position = hole.transform.position;
        rocks.Add(newRock);

        // For animation
        var rockPosition = new RockPosition
        {
            rockRef = rock,
            destinationPosition = newRock.transform.position
        };
        rocksWorldPositions.Add(rockPosition);

        var spriteRender = newRock.GetComponent<SpriteRenderer>();
        if(playerIdent == 'A')
        {
            spriteRender.material.color = playerA;
            return;
        }
        spriteRender.material.color = playerB;
    }

    void AlignRocks(int holeNumber)
    {
        var holeRocks = FindRocksOnHole(holeNumber);

        int id = 0;
        foreach(GameObject rock in holeRocks)
        {
            var rockID = FindRockID(rock);
            float angle = (float)(id / (float)(holeRocks.Count) * 2 * Mathf.PI);
            if(holeNumber < 0)
            {
                char playerHole = holeNumber == -1 ? 'A' : 'B';
                var holeResult = GameObject.Find("HoleResult" + playerHole);

                var width = holeResult.transform.localScale.x * 0.34f;
                var height = holeResult.transform.localScale.y * 0.34f;
                ((RockPosition)rocksWorldPositions[rockID]).destinationPosition =
                holeResult.transform.position + new Vector3(
                    Mathf.Cos(angle) * width,
                    Mathf.Sin(angle) * height,
                    0.0f
                );
                id++;
                continue;
            }
            GameObject hole = GameObject.Find("Hole" + holeNumber);
            var radius = hole.transform.localScale.y * 0.25f;

            ((RockPosition)rocksWorldPositions[rockID]).destinationPosition = 
            hole.transform.position + new Vector3(
                Mathf.Cos(angle) * radius,
                Mathf.Sin(angle) * radius,
                0.0f
            );

            id++;
        }
    }

    ArrayList FindRocksOnHole(int holeNumber)
    {
        var result = new ArrayList();
        for(int i = 0; i < rocksPositions.Count; i++)
        {
            if ((int)rocksPositions[i] == holeNumber)
            {
                result.Add(rocks[i]);
            }

        }
        return result;
    }

    MoveResult ProgressRocks(int holeNumber)
    {
        ArrayList rocksToMove = new ArrayList();
        for(int i = 0; i < rocksPositions.Count; i++)
        {
            if((int)rocksPositions[i] == holeNumber)
            {
                rocksToMove.Add(i);
            }
        }
        if (rocksToMove.Count == 0)
        {
            return MoveResult.None;
        }

        AlignRocks(holeNumber);
        bool pointGiven = false;
        while(rocksToMove.Count > 0)
        {
            holeNumber++;
            int rockToMove = (int)rocksToMove[0];
            if (holeNumber == 6 && !pointGiven)
            {
                rocksPositions[rockToMove] = -1;
                rocksToMove.RemoveAt(0);
                AlignRocks(-1);
                playerAPoints++;
                pointGiven = true;
                holeNumber--;
                continue;
            }
            if(holeNumber == 12 && !pointGiven)
            {
                rocksPositions[rockToMove] = -2;
                rocksToMove.RemoveAt(0);
                AlignRocks(-2);
                playerBPoints++;
                pointGiven = true;
                holeNumber--;
                continue;
            }
            if(holeNumber >= 12)
            {
                holeNumber %= 12;
            }
            pointGiven = false;

            rocksPositions[rockToMove] = holeNumber;
            rocksToMove.RemoveAt(0);
            AlignRocks(holeNumber);
        }
        var rocksOnHole = FindRocksOnHole(holeNumber);

        // Gem Stealing
        int enemyHole = -1;
        if (rocksOnHole.Count == 1)
        {
            if (playerTurn == 'A' && (0 <= holeNumber && holeNumber < 6))
            {
                enemyHole = 11 - holeNumber;
            }
            else if (playerTurn == 'B' && (6 <= holeNumber && holeNumber < 12))
            {
                enemyHole = 11 - holeNumber;

            }
        }
        if(enemyHole != -1)
        {
            Debug.Log($"{holeNumber} : {enemyHole}");
            rocksOnHole = FindRocksOnHole(enemyHole);
            if(rocksOnHole.Count > 0)
            {
                for(int i = 0; i < rocksPositions.Count; i++)
                {
                    if((int)rocksPositions[i] == holeNumber || (int)rocksPositions[i] == enemyHole)
                    {
                        if(playerTurn == 'A') {
                            rocksPositions[i] = -1;
                            AlignRocks(-1);
                            playerAPoints++;
                        } else
                        {
                            rocksPositions[i] = -2;
                            AlignRocks(-2);
                            playerBPoints++;
                        }
                    }
                }
            }
        }

        // Finalizing
        UpdateRockCounters();
        resultTextA.GetComponent<UnityEngine.UI.Text>().text = $"{playerAPoints}";
        resultTextB.GetComponent<UnityEngine.UI.Text>().text = $"{playerBPoints}";
        if(pointGiven)
        {
            return MoveResult.KeepTurn;
        }

        return MoveResult.ChangeTurn;
    }

    void UpdateRockCounters()
    {

        for(int i = 0; i < 12; i++)
        {
            var holeRocks = FindRocksOnHole(i);
            GameObject holeCounter = GameObject.Find("RockCounter" + i);
            if(holeCounter != null )
            {
                holeCounter.GetComponent<Text>().text = holeRocks.Count.ToString();
            }else
            {
                Debug.Log($"Not found {i}");
            }
        }
    }

    RockPosition FindRockPosition(GameObject rock)
    {
        for(int i = 0; i < rocksWorldPositions.Count; i++)
        {
            var currRockPos = (RockPosition)rocksWorldPositions[i];
            if (currRockPos.rockRef == rock)
            {
                return currRockPos;
            }
        }
        return null;
    }
    int FindRockID(GameObject rock)
    {
        for(int i = 0; i < rocks.Count; i++)
        {
            if ((GameObject)rocks[i] == rock)
            {
                return i;
            }
        }
        return -1;

    }

    void Update()
    {
        float dt = Time.deltaTime;
        for(int i = 0; i < rocksWorldPositions.Count; i++)
        {
            var rock = ((GameObject)rocks[i]);
            var rockPos = (RockPosition)rocksWorldPositions[i];
            Vector3 diff = rockPos.destinationPosition - rock.transform.position;

            if(diff.magnitude < ANIM_TOLERANCE) {
                rock.transform.position = rockPos.destinationPosition;
                continue;
            }
            rock.transform.position += ANIM_SPEED * dt * diff;
        }
    }
}


